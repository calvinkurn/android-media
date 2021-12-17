package com.tokopedia.picker.utils

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.RequestPermissionUtil.checkHasPermission

object Permissions {

    fun FragmentActivity?.hasPermissionGranted(): Boolean {
        val hasPermissionCamera = checkHasPermission(this, Manifest.permission.CAMERA)
        val hasPermissionStorage = checkHasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPermissionCamera || hasPermissionStorage
        } else {
            true
        }
    }

}