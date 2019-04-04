package com.tokopedia.videorecorder.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.permissionchecker.PermissionCheckerHelper

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 */

fun exceptionHandler(func: () -> Unit) {
    try {
        func()
    } catch (e: Exception) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
    }
}

internal fun formatter(num: Long): String {
    return String.format("%02d", num)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

internal fun PermissionCheckerHelper.request(activity: Activity, requests: Array<String>, granted: () -> Unit) {
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