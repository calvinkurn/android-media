package com.tokopedia.videorecorder.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.tokopedia.videorecorder.R


/**
 * Created by isfaaghyth on 06/03/19.
 * github: @isfaaghyth
 */
class RuntimePermission(var activity: Activity) {

    companion object {
        private const val RECORD_PERMISSION_REQUEST_CODE = 1
        private const val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2
        private const val CAMERA_PERMISSION_REQUEST_CODE = 3
    }

    fun checkPermissionForRecord(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForExternalStorage(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForCamera(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionForRecord() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Toast.makeText(activity, activity.getString(R.string.vidpick_permission), Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), RECORD_PERMISSION_REQUEST_CODE)
        }
    }

    fun requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    fun requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

}
