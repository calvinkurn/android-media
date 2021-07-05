package com.tokopedia.home_account

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper.PermissionCheckListener
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class PermissionChecker @Inject constructor(val context: Context, val permissionCheckerHelper: PermissionCheckerHelper) {

    fun hasLocationPermission(): Boolean {
        if(context != null) {
            permissionCheckerHelper.let { permission ->
                return permission.hasPermission(context,
                        arrayOf(PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION))
            }
        }
        return false
    }

    fun askLocationPermission(activity: FragmentActivity, listener: PermissionCheckListener){
        permissionCheckerHelper.checkPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), listener, "")
    }
}