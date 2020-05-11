package com.tokopedia.instantloan.ddcollector

interface PermissionResultCallback {
    fun permissionGranted(requestCode: Int)

    fun permissionDenied(requestCode: Int)

    fun neverAskAgain(requestCode: Int)
}
