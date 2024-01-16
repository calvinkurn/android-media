package com.tokopedia.play.widget.player

import com.google.android.exoplayer2.Player
import com.tokopedia.content.common.util.CountDownTimer2
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Created by kenny.hadisaputra on 26/10/23
 */
class VideoPlayerTimer(
    private val exoPlayer: Player,
    private val onTimerFinished: () -> Unit
) {

    private var mLiveTimer: CountDownTimer2? = null

    init {
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                setupInternal(playWhenReady, playbackState)
            }
        })
    }

    fun setup(duration: Duration) {
        mLiveTimer?.release()
        mLiveTimer = getTimer(duration)
        setupInternal(exoPlayer.playWhenReady, exoPlayer.playbackState)
    }

    private fun setupInternal(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED) {
            releaseTimer()
        } else if (playbackState == Player.STATE_READY && playWhenReady) {
            start()
        } else {
            pause()
        }
    }

    private fun getTimer(duration: Duration): CountDownTimer2 {
        return object : CountDownTimer2(
            duration.inWholeMilliseconds,
            1.seconds.inWholeMilliseconds
        ) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                onTimerFinished()
                releaseTimer()
            }
        }
    }

    private fun start() {
        mLiveTimer?.start()
    }

    private fun pause() {
        mLiveTimer?.pause()
    }

    private fun releaseTimer() {
        mLiveTimer?.release()
        mLiveTimer = null
    }
}
