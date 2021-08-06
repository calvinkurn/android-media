package com.tokopedia.imagepicker_insta

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tokopedia.imagepicker_insta.util.CursorUtil

class MainActivity : AppCompatActivity() {
    val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 12
    val CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE = 13

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    var cameraPermissionCallback:((Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (CursorUtil.isStoragePermissionGranted(this)) {
            renderUi()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    fun requestCameraAndWritePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA), CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode ) {
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE->{
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Read permission granted", Toast.LENGTH_SHORT).show()
                    renderUi()
                }
            }
            CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE->{
                cameraPermissionCallback?.invoke(hasAllPermission())
            }

        }
    }

    fun renderUi() {
        setContentView(R.layout.imagepicker_insta_activity_main)
    }

    fun hasAllPermission():Boolean{
        return hasArrayOfPermissions(arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ))
    }

    fun hasArrayOfPermissions(arrayOfPermissions:ArrayList<String>):Boolean{
        var hasAllPermissions = true
        arrayOfPermissions.forEach {
            hasAllPermissions = hasAllPermissions && ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        return hasAllPermissions
    }
}