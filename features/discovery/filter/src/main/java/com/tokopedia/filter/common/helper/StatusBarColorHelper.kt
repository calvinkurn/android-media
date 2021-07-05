package com.tokopedia.filter.common.helper

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat

internal class StatusBarColorHelper(private val activity: Activity) {

    private var color: Int = 0

    init {
        checkBuildVersion {
            color = activity.window.statusBarColor
        }
    }

    fun setStatusBarColor() {
        checkBuildVersion {
            val window = activity.window ?: return@checkBuildVersion

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(activity, com.tokopedia.unifycomponents.R.color.Unify_N700_68)
        }
    }

    fun undoSetStatusBarColor() {
        checkBuildVersion {
            val window = activity.window ?: return@checkBuildVersion

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = color
        }
    }

    private fun checkBuildVersion(action: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            action()
        }
    }
}