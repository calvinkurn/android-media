package com.project.videoplayer.view.player

import com.google.android.exoplayer2.Player
import com.tokopedia.videoplayer.view.player.TkpdVideoPlayer
import com.tokopedia.videoplayer.view.player.VideoPlayerListener
import org.junit.Assert.assertEquals
import org.junit.Test

class TkpdVideoPlayerTest {

    @Test
    fun test_VideoPlayer() {
        TkpdVideoPlayer.Builder()
                .videoSource("https://www.w3schools.com/html/mov_bbb.mp4")
                .listener(object : VideoPlayerListener {
                    override fun onPlayerStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_ENDED -> assertEquals(4, playbackState)
                        }
                    }
                    override fun onPlayerError() {
                        throw Exception("onPlayerError();")
                    }
                })
                .build()
    }

}