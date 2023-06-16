package com.tokopedia.chatbot.chatbot2.view.widget

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.VideoDimensionsListener
import java.io.IOException

class ChatbotExoPlayer(val context: Context, var videoControl: ChatbotVideoControlView? = null) : ChatbotVideoControlView.Listener {

    private var loadControl: LoadControl = DefaultLoadControl.Builder()
        .createDefaultLoadControl()

    var videoStateListener: ChatbotVideoStateListener? = null
    var videoDimensionsListener: VideoDimensionsListener? = null

    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
        .setTrackSelector(DefaultTrackSelector(context))
        .setLoadControl(loadControl)
        .build()

    var isEnded = false

    init {

        exoPlayer.volume = UNMUTE_VOLUME
        exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        videoControl?.player = exoPlayer
        videoControl?.listener = this

        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val isPlaying = playWhenReady && playbackState == Player.STATE_READY
                val isReadyToPlay = !playWhenReady && playbackState == Player.STATE_READY
                val isInitialLoad =
                    (playWhenReady && playbackState != Player.STATE_READY) || (!playWhenReady && playbackState != Player.STATE_READY)

                when {
                    isPlaying -> {
                        videoStateListener?.onVideoReadyToPlay()
                    }
                    playbackState == Player.STATE_ENDED -> {
                        isEnded = true
                    }
                    isReadyToPlay -> {
                        videoStateListener?.onVideoReadyToPlay()
                    }
                    isInitialLoad -> {
                        videoStateListener?.onInitialStateLoading()
                    }
                }
                if (!exoPlayer.playWhenReady && exoPlayer.currentPosition != VIDEO_AT_FIRST_POSITION) {
                    videoStateListener?.onVideoStateChange(
                        exoPlayer.currentPosition,
                        exoPlayer.duration
                    )
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                videoControl?.updateCenterButtonState(isPlaying)
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                videoStateListener?.onVideoPlayerError(error)
            }
        })

        exoPlayer.addAnalyticsListener(object : AnalyticsListener {
            override fun onVideoSizeChanged(
                eventTime: AnalyticsListener.EventTime,
                width: Int,
                height: Int,
                unappliedRotationDegrees: Int,
                pixelWidthHeightRatio: Float
            ) {
                super.onVideoSizeChanged(
                    eventTime,
                    width,
                    height,
                    unappliedRotationDegrees,
                    pixelWidthHeightRatio
                )
                videoDimensionsListener?.setWidth(width)
                videoDimensionsListener?.setHeight(height)
            }
        })
    }

    fun start(videoUrl: String) {
        if (videoUrl.isBlank()) {
            return
        }
        val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl))
        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource, true, false)
    }

    fun resume() {
        exoPlayer.playWhenReady = true
    }

    fun stop() {
        exoPlayer.playWhenReady = false
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun destroy() {
        exoPlayer.release()
    }

    fun getExoPlayer(): SimpleExoPlayer = exoPlayer

    fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        val mDataSourceFactory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))

        val dataSourceFactory = CacheDataSourceFactory(
            MediaPlayerCache.getInstance(context),
            mDataSourceFactory
        )

        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
                .setLoadErrorHandlingPolicy(getMyErrorHandlingPolicy())
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }

    private fun getMyErrorHandlingPolicy(): LoadErrorHandlingPolicy {
        return object : LoadErrorHandlingPolicy {
            override fun getBlacklistDurationMsFor(
                dataType: Int,
                loadDurationMs: Long,
                exception: IOException?,
                errorCount: Int
            ): Long {
                return BLACK_LIST_SECONDS
            }

            override fun getRetryDelayMsFor(
                dataType: Int,
                loadDurationMs: Long,
                exception: IOException?,
                errorCount: Int
            ): Long {
                return RETRY_DELAY
            }

            override fun getMinimumLoadableRetryCount(dataType: Int): Int {
                return RETRY_COUNT
            }
        }
    }
    companion object {
        const val MUTE_VOLUME = 0F
        const val UNMUTE_VOLUME = 1F

        const val VIDEO_AT_FIRST_POSITION = 0L
        const val RETRY_DELAY = 1000L
        const val RETRY_COUNT = 5
        const val BLACK_LIST_SECONDS = 4000L
    }

    override fun onCenterPlayButtonClicked() {
        resume()
        if (isEnded) {
            exoPlayer.seekTo(VIDEO_AT_FIRST_POSITION)
            isEnded = false
        }
    }

    override fun onCenterPauseButtonClicked() {
        pause()
    }

    override fun onScrubStart() {
        pause()
    }

    override fun onScrubStop() {
        resume()
    }

    override fun onScrubMove(position: Long) {
        exoPlayer.seekTo(position)
    }

    interface ChatbotVideoStateListener {
        fun onInitialStateLoading()
        fun onVideoReadyToPlay()
        fun onVideoStateChange(stopDuration: Long, videoDuration: Long)
        fun onVideoPlayerError(e: ExoPlaybackException)
    }
}
