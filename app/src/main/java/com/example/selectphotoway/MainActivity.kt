package com.example.selectphotoway

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val TAG : String = "jeongmin"

    private val REQUEST_CAMERA = 1
    private val REQUEST_OPEN_GALLERY = 2

    // 퍼미션 받을 리스트
    private val requirePermission = arrayOf(
        android.Manifest.permission.CAMERA, // 카메라
        android.Manifest.permission.READ_EXTERNAL_STORAGE // 저장소
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
    }
/* override 함수*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    iv.setImageBitmap(bitmap)
                }
                REQUEST_OPEN_GALLERY -> {
                    val uri = data?.data
                    iv.setImageURI(uri)
                }
            }
        }
    }
/* 일반 함수 */
    // permission 체크 하는 함수
    private fun checkPermission() {
        val status = ContextCompat.checkSelfPermission(this, "requirePermission")
        if(status == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission granted")
        } else {
            requestPermission()
            Log.d(TAG, "permission denied")
        }
    }

    // permission 요청하는 함수
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, requirePermission, 99)
    }

    // 카메라 열기
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    // 갤러리 열기
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
    }
/* onClick 함수 */
    fun onCLickSelect(view: View) {
        var popupMenu = PopupMenu(applicationContext, view)

        menuInflater?.inflate(R.menu.select_menu, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.camera -> {
                    // 카메라 열기
                    openCamera()
                    return@setOnMenuItemClickListener true
                }
                R.id.album -> {
                    // 앨범
                    openGallery()
                    return@setOnMenuItemClickListener true
                }
                R.id.none -> {
                    // 선택 안함
                    iv.setImageResource(R.drawable.person)
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
    }
    }
}