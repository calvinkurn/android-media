package com.tokopedia.buy_more_get_more.olp.utils

import android.R.color.transparent
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

fun Activity.setTransparentStatusBar(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (this.isDarkMode()) {
            this.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    this.let {
        val transparent = MethodChecker.getColor(
            context,
            transparent
        )
        it.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        it.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        WindowCompat.getInsetsController(it.window, it.window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
        it.window.statusBarColor = transparent
    }
}

fun Activity.setDefaultStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (this.isDarkMode()) {
            this.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    WindowCompat.getInsetsController(this.window, this.window.decorView).apply {
        isAppearanceLightStatusBars = true
    }
}

