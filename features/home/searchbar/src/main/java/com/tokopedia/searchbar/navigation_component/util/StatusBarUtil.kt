package com.tokopedia.searchbar.navigation_component.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import java.lang.ref.WeakReference

class StatusBarUtil(private val activityWeakReference: WeakReference<Activity?>) {
    private var isLighStatusBar = true

    fun requestStatusBarDark() {
        //for tokopedia light mode, triggered when position is top of apps, and  it returns transparent background and white text
        //for tokopedia dark mode, triggered when position is top of apps, and it return black background and dark text <- need to be changed to white
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activityWeakReference.get() != null) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                activityWeakReference.get()!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                isLighStatusBar = true
            } else {
                activityWeakReference.get()!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                isLighStatusBar = false
            }
            setWindowFlag(activityWeakReference.get(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activityWeakReference.get()!!.window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun requestStatusBarLight() {
        //for tokopedia light mode, triggered when position not in top of apps, it return white background and dark text
        //for tokopedia dark mode, triggered when position not in top of apps, it returns transparent background and white text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activityWeakReference.get() != null) {
            activityWeakReference.get()!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            setWindowFlag(activityWeakReference.get(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activityWeakReference.get()!!.window.statusBarColor = Color.TRANSPARENT
            isLighStatusBar = true
        }
    }

    companion object {
        fun setWindowFlag(activity: Activity?, bits: Int, on: Boolean) {
            val win = activity!!.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}