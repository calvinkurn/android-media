package com.tokopedia.floatingwindow.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.tokopedia.floatingwindow.exception.FloatingWindowPermissionDeniedException
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

    constructor(context: Context): this(
            context,
            { intent, _ -> context.startActivity(intent) }
    )

    private val permissionActionMap = mutableMapOf<Int, PermissionFlowHandler>()

    private fun isDrawOverOtherAppsEnabled() = context.isDrawOverOtherAppsEnabled()

    fun doPermissionFlow(
            onGranted: () -> Unit,
            onNotGranted: (FloatingWindowPermissionDeniedException) -> Unit,
            onShouldRequestPermission: (RequestPermissionFlow) -> Unit
    ) {
        if (!isDrawOverOtherAppsEnabled() &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // ask for setting
            val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.packageName)
            )

            permissionActionMap[FLOATING_WINDOW_PERMISSION_REQUEST_CODE] = PermissionFlowHandler(onGranted, onNotGranted)

            val requestPermissionFlow = RequestPermissionFlow(intent)
            onShouldRequestPermission(requestPermissionFlow)

        } else onGranted()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FLOATING_WINDOW_PERMISSION_REQUEST_CODE) {
            if (isDrawOverOtherAppsEnabled()) {
                permissionActionMap[FLOATING_WINDOW_PERMISSION_REQUEST_CODE]?.onGranted?.invoke()
            } else {
                permissionActionMap[FLOATING_WINDOW_PERMISSION_REQUEST_CODE]?.onNotGranted?.invoke(FloatingWindowPermissionDeniedException())
            }
            permissionActionMap.remove(FLOATING_WINDOW_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val FLOATING_WINDOW_PERMISSION_REQUEST_CODE = 1241
    }

    data class PermissionFlowHandler(
            val onGranted: () -> Unit,
            val onNotGranted: (FloatingWindowPermissionDeniedException) -> Unit,
    )

    inner class RequestPermissionFlow(private val intent: Intent) {

        fun requestPermission() {
            startActivityForResultHandler(intent, FLOATING_WINDOW_PERMISSION_REQUEST_CODE)
        }

        fun cancel() {
            permissionActionMap.remove(FLOATING_WINDOW_PERMISSION_REQUEST_CODE)
        }
    }
}