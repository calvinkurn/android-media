package com.tokopedia.kotlin.extensions.view

import android.app.Activity
import android.os.Build
import android.view.View

/**
 * Created By @ilhamsuaib on 2020-02-28
 */

fun Activity.setLightStatusBar(isLight: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility =
                if (isLight) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
    } else {
        window.decorView.systemUiVisibility = 0
    }
}
