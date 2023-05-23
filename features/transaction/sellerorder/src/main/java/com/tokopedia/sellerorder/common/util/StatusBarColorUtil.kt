package com.tokopedia.sellerorder.common.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.setWindowFlag

class StatusBarColorUtil(private val activity: Activity) {

    private var color: Int = 0

    init {
        checkBuildVersion {
            color = activity.window.statusBarColor
        }
    }

    fun setStatusBarColor() {
        val window = activity.window ?: return

        with(window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarColor = ContextCompat.getColor(activity, com.tokopedia.unifycomponents.R.color.Unify_N700_68)
            } else {
                statusBarColor = ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black)
            }
        }
    }

    fun undoSetStatusBarColor() {
        checkBuildVersion {
            val window = activity.window ?: return@checkBuildVersion
            with(window) {
                statusBarColor = if (GlobalConfig.isSellerApp()) {
                    color
                } else {
                    Color.TRANSPARENT
                }
            }
        }
    }

    private fun checkBuildVersion(action: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            action()
        }
    }
}