package com.tokopedia.media.picker.utils.delegates

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.RequestPermissionUtil
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun permissionGranted() = PermissionProperty()

class PermissionProperty : ReadOnlyProperty<FragmentActivity, Boolean> {

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): Boolean {
        return thisRef.hasPermissionGranted()
    }

    private fun FragmentActivity?.hasPermissionGranted(): Boolean {
        val hasPermissionCamera = RequestPermissionUtil.checkHasPermission(this, Manifest.permission.CAMERA)
        val hasPermissionAudio = RequestPermissionUtil.checkHasPermission(this, Manifest.permission.RECORD_AUDIO)
        val hasPermissionStorage = RequestPermissionUtil.checkHasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPermissionCamera && hasPermissionAudio && hasPermissionStorage
        } else {
            true
        }
    }

}