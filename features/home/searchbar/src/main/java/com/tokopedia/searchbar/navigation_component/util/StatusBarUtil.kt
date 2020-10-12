package com.tokopedia.searchbar.navigation_component.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import java.lang.ref.WeakReference

class StatusBarUtil(val activity: WeakReference<Activity?>) {
    fun requestStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.get()?.let {
                it.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                setWindowFlag(it, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                it.window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    fun requestStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.get()?.let {
                it.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                setWindowFlag(it, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                it.window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}