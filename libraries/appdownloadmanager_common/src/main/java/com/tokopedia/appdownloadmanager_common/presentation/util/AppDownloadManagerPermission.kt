package com.tokopedia.appdownloadmanager_common.presentation.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.tokopedia.kotlin.extensions.view.ZERO

object AppDownloadManagerPermission {

    private const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 879

    @JvmStatic
    fun checkAndRequestPermission(
        activity: Activity,
        hasGrantPermission: (Boolean) -> Unit
    ) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {
                hasGrantPermission.invoke(true)
            }
        } else {
            hasGrantPermission.invoke(true)
        }
    }

    @JvmStatic
    fun checkRequestPermissionResult(
        requestCode: Int,
        grantResults: IntArray,
        hasGrantPermission: (Boolean) -> Unit
    ) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty()) if (grantResults[Int.ZERO] == PackageManager.PERMISSION_GRANTED) {
                hasGrantPermission.invoke(true)
            }
        }
    }
}
