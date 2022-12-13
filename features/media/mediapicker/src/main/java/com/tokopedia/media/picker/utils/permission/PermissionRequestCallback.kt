package com.tokopedia.media.picker.utils.permission

interface PermissionRequestCallback {
    fun onPermissionPermanentlyDenied(permissions: List<String>)
    fun onDenied(permissions: List<String>)
    fun onGranted(permissions: List<String>)
}
