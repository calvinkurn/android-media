package com.tokopedia.play.broadcaster.util.permission

/**
 * Created by jegul on 07/07/20
 */
interface PermissionHelper {

    fun requestPermission(permission: String)

    fun requestPermissions(permissionList: List<String>)
}