package com.tokopedia.youtube_player

import android.view.View


interface YoutubeCustomViewListener {
    fun onEnterFullScreen(view: View)
    fun onExitFullScreen()
}
