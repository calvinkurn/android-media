package com.tokopedia.screenrecorder

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ScreenRecorderActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PERMISSION_RECORD_SCREEN = 1;
        private val PERMISSIONS = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        )

        private const val REQUEST_MEDIA_PROJECTION = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_recorder_activity_screen_record)
        findViewById<Button>(R.id.btnActivate).setOnClickListener { v -> activateScreenRecorder() }
    }

    private fun activateScreenRecorder() {
        var allPermissionsGranted = true
        for (permission in PERMISSIONS) {
            val granted = ActivityCompat.checkSelfPermission(
                    this,
                    permission
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                allPermissionsGranted = false
                break
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    REQUEST_PERMISSION_RECORD_SCREEN
            );
        } else {
            requestProjectScreen()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_RECORD_SCREEN) {
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    showNeedPermissionsInfo()
                    return
                }
            }
            requestProjectScreen()
        }
    }

    @TargetApi(21)
    private fun requestProjectScreen() {
        val projectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK) {
                startScreenRecordService(resultCode, data)
                finish()
            } else {
                showNeedPermissionsInfo()
            }
        }
    }

    private fun startScreenRecordService(resultCode: Int, data: Intent?) {
        val serviceIntent = Intent(this, ScreenRecordService::class.java)
        serviceIntent.setAction(ScreenRecordService.ACTION_INIT)
        serviceIntent
                .putExtra(ScreenRecordService.EXTRA_MEDIA_PROEJECTION_RESULT_CODE, resultCode)
                .putExtra(ScreenRecordService.EXTRA_MEDIA_PROEJECTION_RESULT_DATA, data)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun showNeedPermissionsInfo() {
        Toast.makeText(this, getString(R.string.screen_recorder_access_denied_info), Toast.LENGTH_SHORT).show()
    }
}
