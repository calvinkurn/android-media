package com.tokopedia.play.view.fragment

import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.tokopedia.keys.Keys

/**
 * Created by jegul on 28/04/20
 */
class PlayYouTubePlayerFragment : YouTubePlayerSupportFragment() {

    fun initialize(listener: YouTubePlayer.OnInitializedListener) {
        initialize(Keys.AUTH_GOOGLE_YOUTUBE_API_KEY, listener)
    }
}