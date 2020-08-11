package com.tokopedia.play.view.fragment

import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment

/**
 * Created by jegul on 28/04/20
 */
class PlayYouTubePlayerFragment : YouTubePlayerSupportFragment() {

    companion object {
        private val YOUTUBE_API_KEY = String(byteArrayOf(65, 73, 122, 97, 83, 121, 67, 82, 107, 103, 119, 71, 66, 101, 56, 90, 120, 106, 99, 75, 48, 55, 67, 110, 108, 51, 65, 117, 102, 55, 50, 66, 112, 103, 65, 54, 108, 76, 111))
    }

    fun initialize(listener: YouTubePlayer.OnInitializedListener) {
        initialize(YOUTUBE_API_KEY, listener)
    }
}