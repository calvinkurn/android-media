package com.tokopedia.imagepicker_insta.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.imagepicker_insta.activity.CameraActivity
import com.tokopedia.imagepicker_insta.activity.PermissionActivity
import com.tokopedia.imagepicker_insta.util.PermissionUtil.MIC_PERMISSION_REQUEST_CODE
import timber.log.Timber

object PermissionUtil {
    val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 12
    val CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE = 13
    val MIC_PERMISSION_REQUEST_CODE = 14

    fun hasAllPermission(context: Context): Boolean {
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
            when (StorageUtil.WRITE_LOCATION) {
                WriteStorageLocation.EXTERNAL ->
                    return hasArrayOfPermissions(
                        context, arrayListOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        )
                    )

                else ->
                    return hasArrayOfPermissions(
                        context, arrayListOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        )
                    )
            }
        } else {
            return hasArrayOfPermissions(
                context, arrayListOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        }
    }

    fun hasArrayOfPermissions(context: Context, arrayOfPermissions: ArrayList<String>): Boolean {
        var hasAllPermissions = true
        arrayOfPermissions.forEach {
            hasAllPermissions = hasAllPermissions && ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        return hasAllPermissions
    }

    fun requestReadPermission(activity: AppCompatActivity) {
        if (!isReadPermissionGranted(activity)) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun requestMicPermission(activity: AppCompatActivity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.RECORD_AUDIO
            ), MIC_PERMISSION_REQUEST_CODE
        )
    }

    fun requestReadPermission(fragment: Fragment) {
        if (!isReadPermissionGranted(fragment.context)) {
            fragment.requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun isReadPermissionGranted(context: Context?): Boolean {
        if (context == null) return false
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    fun hasCameraAndMicPermission(context: Context?): Boolean {
        if (context == null) return false
        return hasArrayOfPermissions(context, arrayListOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
    }

    fun requestCameraAndWritePermission(activity: AppCompatActivity) {
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
            when (StorageUtil.WRITE_LOCATION) {
                WriteStorageLocation.EXTERNAL -> ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE
                )
                WriteStorageLocation.INTERNAL -> ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                CAMERA_AND_WRITE_PERMISSION_REQUEST_CODE
            )
        }

    }


}

fun CameraActivity.requestCameraAndMicPermission() {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), MIC_PERMISSION_REQUEST_CODE)
}

fun PermissionActivity.handleOnRequestPermissionsResult(grantResults: IntArray){
    if (grantResults.isNotEmpty()) {
        val deniedPermissionList = grantResults.filter {
            it == PackageManager.PERMISSION_DENIED
        }
        if (!deniedPermissionList.isNullOrEmpty()) {
            permissionDeniedCount += 1

            if (permissionDeniedCount > 2) {
                openAppSystemSettings()
            }
        }
    }
}

fun Context.openAppSystemSettings() {
    try {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        })
    } catch (th: Throwable) {
        Timber.e(th)
    }
}