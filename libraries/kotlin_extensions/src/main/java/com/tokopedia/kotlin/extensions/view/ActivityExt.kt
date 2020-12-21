package com.tokopedia.kotlin.extensions.view

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi

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

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.requestStatusBarLight() {
    window?.run {
        decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        statusBarColor = Color.TRANSPARENT
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.requestStatusBarDark() {
    window?.run {
        decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        statusBarColor = Color.TRANSPARENT
    }
}

fun Window.setWindowFlag(bits: Int, on: Boolean) {
    val winParams: WindowManager.LayoutParams = this.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    this.attributes = winParams
}

fun Activity.setupStatusBarUnderMarshmallow() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
    if (Build.VERSION.SDK_INT >= 19) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 21) {
        window?.run {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            statusBarColor = Color.TRANSPARENT
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarColor(color: Int) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
    window.statusBarColor = color;
}

fun Activity.getIntIntentExtra(key: String, defValue: Int): Lazy<Int> {
    return lazy {
        intent?.getIntExtra(key, defValue) ?: defValue
    }
}
