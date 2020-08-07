package com.tokopedia.play.broadcaster.util.permission

import android.content.pm.PackageManager

/**
 * Created by jegul on 08/07/20
 */
typealias PermissionResultMap = Map<String, Int>
typealias PermissionStatusHandler = PermissionStatusManager.() -> Unit

class PermissionStatusManager(private val permissionResultMap: PermissionResultMap, val requestCode: Int) {

    fun isGranted(permission: String): Boolean {
        return permissionResultMap[permission] == PackageManager.PERMISSION_GRANTED
    }

    fun isDenied(permission: String): Boolean {
        return permissionResultMap[permission] == PackageManager.PERMISSION_DENIED
    }

    fun isAllGranted(): Boolean = permissionResultMap.all { isGranted(it.key) }
}