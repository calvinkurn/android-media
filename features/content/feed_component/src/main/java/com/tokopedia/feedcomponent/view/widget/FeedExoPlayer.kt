package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.device.info.DeviceConnectionInfo

class FeedExoPlayer(val context: Context) {

    private var videoStateListener: VideoStateListener? = null

    private var loadControl: LoadControl = DefaultLoadControl()

    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
        .setTrackSelector(DefaultTrackSelector(context))
        .setLoadControl(loadControl)
        .build()

    private val isConnectedToWifi: Boolean = DeviceConnectionInfo.isConnectWifi(context)

    init {
        exoPlayer.volume = MUTE_VOLUME
        exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val isPlaying = playWhenReady && playbackState == Player.STATE_READY
                val isReadyToPlay = !playWhenReady && playbackState == Player.STATE_READY
                val isInitialLoad =
                    (playWhenReady && playbackState != Player.STATE_READY) || (!playWhenReady && playbackState != Player.STATE_READY)

                when {
                    isPlaying || playbackState == Player.STATE_ENDED -> {
                        videoStateListener?.onVideoReadyToPlay()
                    }
                    isReadyToPlay -> {
                        videoStateListener?.onVideoReadyToPlay()
                    }
                    isInitialLoad -> {
                        videoStateListener?.onInitialStateLoading()
                    }
                }
            }
        })
    }

    fun reset() {
        exoPlayer.seekTo(1)
    }

    fun setVideoResizeListener(listener: VideoListener) {
        exoPlayer.addVideoListener(listener)
    }

    fun setVideoStateListener(videoStateListener: VideoStateListener?) {
        this.videoStateListener = videoStateListener
    }

    fun start(videoUrl: String, isMute: Boolean) {
        if (videoUrl.isBlank()) return

        val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl))
        toggleVideoVolume(isMute)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
        exoPlayer.playWhenReady = isConnectedToWifi
        exoPlayer.prepare(mediaSource, true, false)
    }

    fun resume() {
        reset()
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

    fun toggleVideoVolume(isMute: Boolean) {
        if (isMute) {
            exoPlayer.volume = MUTE_VOLUME
        } else {
            exoPlayer.volume = UNMUTE_VOLUME
        }
        videoStateListener?.configureVolume(isMute)
    }

    fun getExoPlayer(): SimpleExoPlayer = exoPlayer

    fun isMute(): Boolean = exoPlayer.volume == MUTE_VOLUME

    private fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        val mDataSourceFactory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(mDataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(mDataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(mDataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(mDataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }

    companion object {
        const val MUTE_VOLUME = 0F
        const val UNMUTE_VOLUME = 1F

        const val VIDEO_AT_FIRST_POSITION = 0L
    }
}

interface VideoStateListener {
    fun onInitialStateLoading()
    fun onVideoReadyToPlay()
    fun configureVolume(isMute: Boolean)
}