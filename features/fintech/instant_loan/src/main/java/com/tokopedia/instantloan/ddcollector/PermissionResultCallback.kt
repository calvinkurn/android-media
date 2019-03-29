package com.tokopedia.instantloan.ddcollector

/**
 * Created by Jaison on 25/08/16.
 */

interface PermissionResultCallback {
    fun permissionGranted(requestCode: Int)

    fun permissionDenied(requestCode: Int)

    fun neverAskAgain(requestCode: Int)
}