package com.tokopedia.appdownloadmanager_common.presentation.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object AppDownloadManagerPermission {

    const val PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 879

    val requiredPermissions: Array<String>
        get() = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

    @JvmStatic
    fun checkAndRequestPermission(
        activity: Activity,
        hasGrantPermission: (Boolean) -> Unit
    ) {
        val isAllPermissionNotGranted = requiredPermissions.any {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (isAllPermissionNotGranted) {
            ActivityCompat.requestPermissions(
                activity,
                requiredPermissions,
                PERMISSIONS_REQUEST_EXTERNAL_STORAGE
            )
        } else {
            hasGrantPermission.invoke(true)
        }
    }

    @JvmStatic
    fun checkRequestPermissionResult(
        grantResults: IntArray,
        requestCode: Int,
        hasGrantPermission: (Boolean) -> Unit
    ) {
        if (requestCode == PERMISSIONS_REQUEST_EXTERNAL_STORAGE) {
            val isAllGrantedPermission =
                grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            hasGrantPermission.invoke(isAllGrantedPermission)
        }
    }

    fun isAllPermissionNotGranted(activity: Activity): Boolean {
        return requiredPermissions.any {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
    }
}
