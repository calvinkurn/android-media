package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.feedcomponent.util.FeedExoUtil
import com.tokopedia.feedcomponent.util.FeedVideoCache
import java.util.concurrent.TimeUnit

class FeedExoPlayer(val context: Context) {

    private var videoStateListener: VideoStateListener? = null

    private var loadControl: LoadControl = DefaultLoadControl.Builder()
        .setBackBuffer(
            TimeUnit.MINUTES.toMillis(1).toInt(),
            true
        )
        .setBufferDurationsMs(
            TimeUnit.SECONDS.toMillis(30).toInt(), // this is expected
            DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
            TimeUnit.SECONDS.toMillis(1).toInt(),
            TimeUnit.SECONDS.toMillis(1).toInt()
        ).createDefaultLoadControl()

    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
        .setTrackSelector(DefaultTrackSelector(context))
        .setLoadControl(loadControl)
        .build()

    init {
        exoPlayer.volume = MUTE_VOLUME
        exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val isPlaying = playWhenReady && playbackState == Player.STATE_READY
                val isReadyToPlay = !playWhenReady && playbackState == Player.STATE_READY
                val isBuffering = playbackState == Player.STATE_BUFFERING
                val isInitialLoad =
                    (playWhenReady && playbackState != Player.STATE_READY) || (!playWhenReady && playbackState != Player.STATE_READY)

                when {
                    isPlaying || playbackState == Player.STATE_ENDED -> {
                        videoStateListener?.onVideoReadyToPlay(playWhenReady)
                    }
                    isReadyToPlay -> {
                        videoStateListener?.onVideoReadyToPlay(false)
                    }
                    isBuffering -> {
                        videoStateListener?.onBuffering()
                    }
                    isInitialLoad -> {
                        videoStateListener?.onInitialStateLoading()
                    }
                }
                if (!exoPlayer.playWhenReady && exoPlayer.currentPosition != VIDEO_AT_FIRST_POSITION) {
                    // Track only when video stop
                    videoStateListener?.onVideoStateChange(
                        exoPlayer.currentPosition,
                        exoPlayer.duration
                    )
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)

                if (error.type != ExoPlaybackException.TYPE_SOURCE) return
                if (error.sourceException !is BehindLiveWindowException) return

                videoStateListener?.onBehindLiveWindow(exoPlayer.playWhenReady)
            }
        })
    }

    fun reset() {
        exoPlayer.seekTo(0)
    }

    fun setVideoResizeListener(listener: VideoListener) {
        exoPlayer.addVideoListener(listener)
    }

    fun setVideoStateListener(videoStateListener: VideoStateListener?) {
        this.videoStateListener = videoStateListener
    }

    fun start(videoUrl: String, isMute: Boolean, playWhenReady: Boolean = true, isLive: Boolean = false) {
        if (videoUrl.isBlank()) return

        val mediaSource = FeedExoUtil.getMediaSourceByUri(
            context,
            Uri.parse(videoUrl),
            if (!isLive) FeedVideoCache.getInstance(context).cache else null
        )
        toggleVideoVolume(isMute)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.prepare(mediaSource, true, false)
    }

    fun resume(shouldReset: Boolean = true) {
        if (shouldReset) reset()
        exoPlayer.playWhenReady = true
    }
    fun replay() {
        if (exoPlayer.playbackState == ExoPlayer.STATE_ENDED) reset()
        exoPlayer.playWhenReady = true
    }

    fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun destroy() {
        exoPlayer.release()
    }

    fun release() {
        exoPlayer.release()
    }

    fun toggleVideoVolume(isMute: Boolean) {
        if (isMute) {
            exoPlayer.volume = MUTE_VOLUME
        } else {
            exoPlayer.volume = UNMUTE_VOLUME
        }
    }

    fun getExoPlayer(): SimpleExoPlayer = exoPlayer

    fun isMute(): Boolean = exoPlayer.volume == MUTE_VOLUME

    companion object {
        const val MUTE_VOLUME = 0F
        const val UNMUTE_VOLUME = 1F

        const val VIDEO_AT_FIRST_POSITION = 0L
    }
}

interface VideoStateListener {
    fun onInitialStateLoading()
    fun onVideoReadyToPlay(isPlaying: Boolean)

    fun onBuffering() {}
    fun onVideoStateChange(stopDuration: Long, videoDuration: Long) // Tracker Purpose

    fun onBehindLiveWindow(playWhenReady: Boolean) {}
}
