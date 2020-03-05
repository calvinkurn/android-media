package com.tokopedia.play.util

import android.app.Activity
import android.os.Build
import android.view.View

/**
 * Created by jegul on 10/01/20
 */
object PlayFullScreenHelper {

    fun hideSystemUi(activity: Activity) {
        val uiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) it or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            else it
        }

        activity.window.decorView.systemUiVisibility = uiVisibility
    }

    fun showSystemUi(activity: Activity) {
        activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}