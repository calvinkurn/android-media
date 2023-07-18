package com.tokopedia.sellerorder.common.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import com.tokopedia.config.GlobalConfig
import java.lang.ref.WeakReference

class StatusBarColorUtil(activity: Activity) {

    private var color: Int = 0

    var activityRef: WeakReference<Activity>? = null

    init {
        activityRef = WeakReference(activity)
        checkBuildVersion {
            val getActivity = activityRef?.get() ?: return@checkBuildVersion
            color = getActivity.window.statusBarColor
        }
    }

    fun setStatusBarColor() {
        val getActivity = activityRef?.get() ?: return
        val window = getActivity.window ?: return

        with(window) {
            statusBarColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ContextCompat.getColor(getActivity, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
            } else {
                ContextCompat.getColor(getActivity, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black)
            }
        }
    }

    fun undoSetStatusBarColor() {
        checkBuildVersion {
            val getActivity = activityRef?.get() ?: return@checkBuildVersion
            val window = getActivity.window ?: return@checkBuildVersion
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
