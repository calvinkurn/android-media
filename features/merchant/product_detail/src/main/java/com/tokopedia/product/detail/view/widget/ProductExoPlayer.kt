package com.tokopedia.product.detail.view.widget

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


/**
 * Created by Yehezkiel on 23/11/20
 * ExoPlayer need to re-prepare if video url is changed, and this causing loading time when first open video.
 * So we only prepare when needed, if next destination is also video type which means we need to change url. So we prepare it
 * If next destination is not video type (image type) just pause the video.
 */
class ProductExoPlayer(val context: Context) {

    private var videoStateListener: VideoStateListener? = null

    private var loadControl: LoadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(MIN_BUFFER_DURATION,
                    MAX_BUFFER_DURATION,
                    MIN_PLAYBACK_START_BUFFER,
                    MIN_PLAYBACK_RESUME_BUFFER)
            .createDefaultLoadControl()

    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context))
            .setLoadControl(loadControl)
            .build()

    private val isConnectedToWifi: Boolean = DeviceConnectionInfo.isConnectWifi(context)

    init {
        exoPlayer.volume = MUTE_VOLUME
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val isPlaying = playWhenReady && playbackState == Player.STATE_READY
                val isReadyToPlay = !playWhenReady && playbackState == Player.STATE_READY
                val isInitialLoad = (playWhenReady && playbackState != Player.STATE_READY) || (!playWhenReady && playbackState != Player.STATE_READY)

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
                    else -> videoStateListener?.onVideoBuffering()
                }

                if (!playWhenReady && exoPlayer.currentPosition != VIDEO_AT_FIRST_POSITION && playbackState == ExoPlayer.STATE_READY) {
                    //Track only when video stop
                    videoStateListener?.onVideoStateChange(exoPlayer.currentPosition, exoPlayer.duration)
                }
            }
        })
    }

    fun setVideoResizeListener(listener: VideoListener) {
        exoPlayer.addVideoListener(listener)
    }

    fun setVideoStateListener(videoStateListener: VideoStateListener?) {
        this.videoStateListener = videoStateListener
    }

    /**
     * @shouldPrepare
     * True if last video is different with current video
     * False if last video is same with current video (means we open the same video as before)
     */
    fun start(videoUrl: String, lastVideoPosition: Long, isMute: Boolean, shouldPrepare: Boolean = true) {
        if (videoUrl.isBlank()) return

        val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl))

        if (lastVideoPosition != VIDEO_AT_FIRST_POSITION && shouldPrepare) {
            exoPlayer.seekTo(lastVideoPosition)
        }
        toggleVideoVolume(isMute)
        exoPlayer.playWhenReady = isConnectedToWifi
        prepareIfVideoDifferent(mediaSource, lastVideoPosition, shouldPrepare)
    }

    fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun resumeAndSeekTo(lastVideoPosition: Long) {
        if (lastVideoPosition != VIDEO_AT_FIRST_POSITION) {
            exoPlayer.seekTo(lastVideoPosition)
        }
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

    private fun prepareIfVideoDifferent(mediaSource: MediaSource, lastVideoPosition: Long, shouldPrepare: Boolean) {
        if (shouldPrepare) exoPlayer.prepare(mediaSource, lastVideoPosition == VIDEO_AT_FIRST_POSITION, false)
    }

    private fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        val mDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
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
        //Minimum Video you want to buffer while Playing
        private const val MIN_BUFFER_DURATION = 3000

        //Max Video you want to buffer during PlayBack
        private const val MAX_BUFFER_DURATION = 18000

        //Min Video you want to buffer before start Playing it
        private const val MIN_PLAYBACK_START_BUFFER = 2000

        //Min video You want to buffer when user resumes video
        private const val MIN_PLAYBACK_RESUME_BUFFER = 2000

        const val MUTE_VOLUME = 0F
        const val UNMUTE_VOLUME = 1F

        const val VIDEO_AT_FIRST_POSITION = 0L
    }
}

interface VideoStateListener {
    fun onInitialStateLoading()
    fun onVideoReadyToPlay()
    fun onVideoBuffering()
    fun configureVolume(isMute: Boolean)
    fun onVideoStateChange(stopDuration: Long, videoDuration: Long) //Tracker Purpose
}