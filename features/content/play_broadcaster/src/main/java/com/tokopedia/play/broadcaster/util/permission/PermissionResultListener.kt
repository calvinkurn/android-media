package com.tokopedia.play.broadcaster.util.permission

/**
 * Created by jegul on 07/07/20
 */
interface PermissionResultListener {

    fun onPermissionGranted(permissionList: List<String>)

    fun onPermissionDenied(permissionList: List<String>)

    fun onShouldShowRationale()
}