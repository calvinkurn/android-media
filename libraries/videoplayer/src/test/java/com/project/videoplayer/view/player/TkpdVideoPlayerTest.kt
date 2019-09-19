package com.project.videoplayer.view.player

import com.google.android.exoplayer2.Player
import com.tokopedia.videoplayer.state.PlayerController
import com.tokopedia.videoplayer.state.PlayerException
import com.tokopedia.videoplayer.state.PlayerType
import com.tokopedia.videoplayer.state.RepeatMode
import com.tokopedia.videoplayer.view.player.TkpdVideoPlayer
import com.tokopedia.videoplayer.view.player.VideoPlayerListener
import org.junit.Assert.assertEquals
import org.junit.Test

class TkpdVideoPlayerTest {

    @Test fun test_VideoPlayer() {
        TkpdVideoPlayer.Builder()
                .videoSource("https://www.w3schools.com/html/mov_bbb.mp4")
                .type(PlayerType.DEFAULT)
                .repeatMode(RepeatMode.REPEAT_MODE_OFF)
                .controller(PlayerController.ON)
                .listener(object : VideoPlayerListener {
                    override fun onPlayerStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_IDLE -> {
                                println("STATE_IDLE")
                                assertEquals(1, playbackState)
                            }
                            Player.STATE_BUFFERING -> {
                                println("STATE_BUFFERING")
                                assertEquals(2, playbackState)
                            }
                            Player.STATE_READY -> {
                                println("STATE_READY")
                                assertEquals(3, playbackState)
                            }
                            Player.STATE_ENDED -> {
                                println("STATE_ENDED")
                                assertEquals(4, playbackState)
                            }
                        }
                    }

                    override fun onPlayerError(error: PlayerException) {
                        throw Exception("onPlayerError();")
                    }
                })
                .build()
    }

}