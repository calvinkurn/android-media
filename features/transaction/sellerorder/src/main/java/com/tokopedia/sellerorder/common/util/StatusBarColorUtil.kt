package com.tokopedia.sellerorder.common.util

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.setWindowFlag

class StatusBarColorUtil(private val activity: Activity) {

    private var color: Int = 0

    init {
        checkBuildVersion {
            color = activity.window.statusBarColor
        }
    }

    fun setStatusBarColor() {
        checkBuildVersion {
            val window = activity.window ?: return@checkBuildVersion

            with(window) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                statusBarColor = ContextCompat.getColor(activity, com.tokopedia.unifycomponents.R.color.Unify_N700_68)
            }
        }
    }

    fun undoSetStatusBarColor() {
        checkBuildVersion {
            val window = activity.window ?: return@checkBuildVersion
            with(window) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                statusBarColor = color
            }
        }
    }

    private fun checkBuildVersion(action: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            action()
        }
    }
}