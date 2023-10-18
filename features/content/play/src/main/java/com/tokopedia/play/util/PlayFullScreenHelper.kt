package com.tokopedia.play.util

import android.view.View

/**
 * Created by jegul on 10/01/20
 */
object PlayFullScreenHelper {

    fun getShowSystemUiVisibility() = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    fun getHideSystemUiVisibility(): Int {
        return (
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            ).let { it or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY }
    }

}
