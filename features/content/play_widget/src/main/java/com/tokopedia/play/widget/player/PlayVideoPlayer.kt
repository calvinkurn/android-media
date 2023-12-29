package com.tokopedia.play.widget.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.content.common.util.CountDownTimer2
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.play_common.util.PlayConnectionCommon
import java.util.concurrent.TimeUnit

/**
 * Created by mzennis on 09/10/20.
 */
open class PlayVideoPlayer(val context: Context, cardType: PlayWidgetType) {
    var listener: VideoPlayerListener? = null
    var videoUrl: String? = null

    var isLive: Boolean = false

    var maxDurationCellularInSeconds: Int = 0
    var maxDurationWifiInSeconds: Int = 0

    private var mLiveTimer: CountDownTimer2? = null

    private val shouldForceLowest = cardType != PlayWidgetType.Jumbo

    private val trackSelector = DefaultTrackSelector(context).apply {
        parameters = DefaultTrackSelector.ParametersBuilder(context)
            .setForceLowestBitrate(shouldForceLowest).build()
    }

    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
        .setTrackSelector(trackSelector)
        .build()

    init {
        exoPlayer.volume = 0F
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        whenIsPlayingChanged(isPlaying = false)
                    }
                    Player.STATE_READY -> {
                        whenIsPlayingChanged(isPlaying = true)
                    }
                    Player.STATE_BUFFERING -> {
                        // don't do anything when buffering, just follow the previous state
                    }
                    else -> whenIsPlayingChanged(isPlaying = false)
                }
            }
        })

        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    releaseTimer()
                    return
                }

                if (playbackState == Player.STATE_READY && playWhenReady) {
                    mLiveTimer?.start()
                } else {
                    mLiveTimer?.pause()
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)

                if (error.type != ExoPlaybackException.TYPE_SOURCE) return
                if (error.sourceException !is BehindLiveWindowException) return

                initPlayer(shouldRestartTimer = false)
            }
        })
    }

    private fun whenIsPlayingChanged(isPlaying: Boolean) {
        listener?.onIsPlayingChanged(isPlaying)
    }

    fun start() {
        releaseTimer()
        initPlayer()
    }

    private fun initPlayer(shouldRestartTimer: Boolean = true) {
        if (videoUrl == null || videoUrl?.isBlank() == true) return

        val maxDuration = if (PlayConnectionCommon.isConnectCellular(context)) {
            maxDurationCellularInSeconds
        } else {
            maxDurationWifiInSeconds
        }.toLong()

        val mediaSource = getMediaSourceBySource(
            context,
            Uri.parse(videoUrl),
            isLive,
            maxDuration
        )

        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource, true, false)

        if (isLive && shouldRestartTimer) {
            mLiveTimer = getLiveTimer(maxDuration)
        }
    }

    fun mute(shouldMute: Boolean) {
        exoPlayer.volume = if (shouldMute) 0f else 100f
    }

    fun repeat(shouldRepeat: Boolean) {
        exoPlayer.repeatMode = if (shouldRepeat) ExoPlayer.REPEAT_MODE_ONE else ExoPlayer.REPEAT_MODE_OFF
    }

    fun stop() {
        releaseTimer()
        exoPlayer.stop()
    }

    fun restart() {
        start()
    }

    fun release() {
        try {
            exoPlayer.release()
        } catch (_: Throwable) {
        }
    }

    fun getPlayer(): ExoPlayer = exoPlayer

    interface VideoPlayerListener {
        fun onIsPlayingChanged(isPlaying: Boolean)
    }

    private fun getMediaSourceBySource(
        context: Context,
        uri: Uri,
        isLive: Boolean,
        durationToPlayInSecond: Long
    ): MediaSource {
        val defaultDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
        val dataSourceFactory = if (!isLive) {
            CacheDataSourceFactory(PlayWidgetVideoCache.getInstance(context), defaultDataSourceFactory)
        } else {
            defaultDataSourceFactory
        }

        val mediaSourceFactory = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }

        return if (durationToPlayInSecond <= 0 || isLive) {
            mediaSourceFactory.createMediaSource(uri)
        } else {
            ClippingMediaSource(
                mediaSourceFactory.createMediaSource(uri),
                0,
                TimeUnit.SECONDS.toMicros(durationToPlayInSecond),
                true,
                true,
                true
            )
        }
    }

    private fun getLiveTimer(durationInSeconds: Long): CountDownTimer2 {
        return object : CountDownTimer2(
            TimeUnit.SECONDS.toMillis(durationInSeconds),
            TimeUnit.SECONDS.toMillis(1)
        ) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                stop()
            }
        }
    }

    private fun releaseTimer() {
        mLiveTimer?.release()
        mLiveTimer = null
    }
}
