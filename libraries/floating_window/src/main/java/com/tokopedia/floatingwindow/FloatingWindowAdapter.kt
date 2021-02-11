package com.tokopedia.floatingwindow

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import com.tokopedia.floatingwindow.exception.FloatingWindowException
import com.tokopedia.floatingwindow.permission.FloatingWindowPermissionManager
import com.tokopedia.floatingwindow.view.FloatingWindowView

/**
 * Created by jegul on 26/11/20
 */
class FloatingWindowAdapter private constructor(
        private val context: Context,
        private val permissionManager: FloatingWindowPermissionManager
) {

    constructor(fragment: Fragment): this(
            fragment.requireContext(),
            FloatingWindowPermissionManager(fragment)
    )

    constructor(activity: Activity): this(
            activity as Context,
            FloatingWindowPermissionManager(activity)
    )

    constructor(context: Context): this(
            context,
            FloatingWindowPermissionManager(context)
    )

    private val floatingWindow: FloatingWindow
        get() = FloatingWindow.getInstance(context)

    fun addView(
            floatingView: FloatingWindowView,
            onSuccess: () -> Unit = {},
            onShouldRequestPermission: (FloatingWindowPermissionManager.RequestPermissionFlow) -> Unit = { it.requestPermission() },
            onFailure: (FloatingWindowException) -> Unit = {},
            overwrite: Boolean = false
    ) {
        permissionManager.doPermissionFlow(
                onGranted = {
                    floatingWindow.addView(
                            key = floatingView.key,
                            floatingView = floatingView,
                            overwrite = overwrite
                    )
                    onSuccess()
                },
                onNotGranted = onFailure,
                onShouldRequestPermission = onShouldRequestPermission
        )
    }

    fun contains(key: String): Boolean {
        return floatingWindow.getViewByKey(key) != null
    }

    fun removeByKey(key: String) {
        floatingWindow.removeByKey(key)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        permissionManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun getCurrentDisplayMetrics(): DisplayMetrics {
        val dm = DisplayMetrics()
        floatingWindow.getWindowManager().defaultDisplay.getMetrics(dm)
        return dm
    }
}