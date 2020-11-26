package com.tokopedia.floatingwindow

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.floatingwindow.permission.FloatingWindowPermissionManager

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

    private val floatingWindow: FloatingWindow
        get() = FloatingWindow.getInstance(context)

    private val displayMetrics = getCurrentDisplayMetrics()

    fun addView(
            key: String,
            view: View,
            width: Int,
            height: Int,
            x: Int = (displayMetrics.widthPixels - width) / 2,
            y: Int = (displayMetrics.heightPixels - height) / 2,
            gravity: Int = getWindowManagerDefaultGravity(),
            onFailure: () -> Unit = {},
            overwrite: Boolean = false
    ) {
        permissionManager.doPermissionFlow(
                onGranted = {
                    floatingWindow.addView(
                            key = key,
                            view = view,
                            layoutParams = getWindowManagerLayoutParams(width, height, x, y, gravity),
                            overwrite = overwrite
                    )
                },
                onNotGranted = onFailure
        )
    }

    fun removeByKey(key: String) {
        floatingWindow.removeByKey(key)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        permissionManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun getWindowManagerLayoutParams(
            width: Int,
            height: Int,
            x: Int,
            y: Int,
            gravity: Int
    ): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
                width,
                height,
                x,
                y,
                getWindowManagerLayoutParamsType(),
                getWindowManagerFlags(),
                PixelFormat.TRANSLUCENT
        ).apply {
            this.gravity = gravity
        }
    }

    private fun getWindowManagerLayoutParamsType(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
    }

    private fun getWindowManagerFlags(): Int {
        return WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
    }

    private fun getWindowManagerDefaultGravity(): Int {
        return Gravity.TOP or Gravity.START
    }

    private fun getCurrentDisplayMetrics(): DisplayMetrics {
        val dm = DisplayMetrics()
        floatingWindow.getWindowManager().defaultDisplay.getMetrics(dm)
        return dm
    }
}