package com.tokopedia.utils.permission

import androidx.fragment.app.FragmentActivity

/**
 * Created by isfaaghyth on 05/04/19.
 * github: @isfaaghyth
 *
 * How to use it?
 * first, init of permissions of array list (i.e. arrayOf(PermissionCheckerHelper.Companion.PERMISSION_CAMERA, ...))
 * finally, do something when is granted.
 *
 * @example
 * pcHelper.request(this, permissions()) {
 *      //do something
 * }
 */

fun PermissionCheckerHelper.request(
        activity: FragmentActivity,
        requests: Array<String>,
        granted: () -> Unit) {
    this.checkPermissions(activity,
            requests,
            object : PermissionCheckerHelper.PermissionCheckListener {
                override fun onPermissionDenied(permissionText: String) {
                    onPermissionDenied(activity, permissionText)
                    activity.finish()
                }

                override fun onNeverAskAgain(permissionText: String) {
                    onNeverAskAgain(activity, permissionText)
                    activity.finish()
                }

                override fun onPermissionGranted() {
                    granted()
                }
            }
    )
}

fun PermissionCheckerHelper.request(
        activity: FragmentActivity,
        requests: Array<String>,
        granted: () -> Unit, denied: () -> Unit) {
    this.checkPermissions(activity,
            requests,
            object : PermissionCheckerHelper.PermissionCheckListener {
                override fun onPermissionDenied(permissionText: String) {
                    onPermissionDenied(activity, permissionText)
                    denied()
                }

                override fun onNeverAskAgain(permissionText: String) {
                    onNeverAskAgain(activity, permissionText)
                    activity.finish()
                }

                override fun onPermissionGranted() {
                    granted()
                }
            }
    )
}