package com.tokopedia.imagepicker_insta.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.util.CursorUtil

class MainActivity : AppCompatActivity() {

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
            PermissionUtil.requestReadPermission(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode ) {
            PermissionUtil.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE ->{
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Read permission granted", Toast.LENGTH_SHORT).show()
                    renderUi()
                }
            }
            PermissionUtil.CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE ->{
                cameraPermissionCallback?.invoke(PermissionUtil.hasAllPermission(this))
            }

        }
    }

    fun renderUi() {
        setContentView(R.layout.imagepicker_insta_activity_main)
    }
}