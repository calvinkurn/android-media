package com.tokopedia.floatingwindow.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.tokopedia.floatingwindow.util.isDrawOverOtherAppsEnabled

/**
 * Created by jegul on 26/11/20
 */
class FloatingWindowPermissionManager private constructor(
        private val context: Context,
        private val startActivityForResultHandler: (intent: Intent, requestCode: Int) -> Unit
) {

    constructor(fragment: Fragment): this(
            fragment.requireContext(),
            { intent, requestCode -> fragment.startActivityForResult(intent, requestCode) }
    )

    constructor(activity: Activity): this(
            activity as Context,
            { intent, requestCode -> activity.startActivityForResult(intent, requestCode) }
    )

    private val permissionActionMap = mutableMapOf<Int, PermissionFlowHandler>()

    private fun isDrawOverOtherAppsEnabled() = context.isDrawOverOtherAppsEnabled()

    fun doPermissionFlow(
            onGranted: () -> Unit,
            onNotGranted: () -> Unit
    ) {
        if (!isDrawOverOtherAppsEnabled()) {
            // ask for setting
            val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.packageName)
            )

            permissionActionMap[FLOAWY_PERMISSION_REQUEST_CODE] = PermissionFlowHandler(onGranted, onNotGranted)

            startActivityForResultHandler(intent, FLOAWY_PERMISSION_REQUEST_CODE)
        } else onGranted()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FLOAWY_PERMISSION_REQUEST_CODE) {
            if (isDrawOverOtherAppsEnabled()) {
                permissionActionMap[FLOAWY_PERMISSION_REQUEST_CODE]?.onGranted?.invoke()
            } else {
                permissionActionMap[FLOAWY_PERMISSION_REQUEST_CODE]?.onNotGranted?.invoke()
            }
            permissionActionMap.remove(FLOAWY_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val FLOAWY_PERMISSION_REQUEST_CODE = 1241
    }

    data class PermissionFlowHandler(
            val onGranted: () -> Unit,
            val onNotGranted: () -> Unit,
    )
}