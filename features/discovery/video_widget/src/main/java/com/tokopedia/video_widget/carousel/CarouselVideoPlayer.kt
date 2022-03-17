package com.tokopedia.video_widget.carousel

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.video_widget.VideoPlayerCache
import java.lang.ref.WeakReference

class CarouselVideoPlayer(context: Context) {
    companion object {
        private const val DEFAULT_CLIP_DURATION = 5_000_000L // 5 second in microsecond
    }

    private val contextReference = WeakReference(context)
    private val context: Context?
        get() = contextReference.get()
    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
    private var autoStopTimer: CountDownTimer? = null

    var listener: VideoPlayerListener? = null
    var videoUrl: String? = null
    var shouldCache: Boolean = false

    var maxDurationCellularInSeconds: Int? = null

    init {
        exoPlayer.volume = 0F
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> if (DeviceConnectionInfo.isConnectCellular(context)) whenIsPlayingChanged(
                        isPlaying = false
                    )
                    Player.STATE_READY -> {
                        if (playWhenReady && DeviceConnectionInfo.isConnectCellular(context)) {
                            configureAutoStop(maxDurationCellularInSeconds)
                        }

                        whenIsPlayingChanged(isPlaying = true)
                    }
                    else -> whenIsPlayingChanged(isPlaying = false)
                }
            }
        })
    }

    private fun whenIsPlayingChanged(isPlaying: Boolean) {
        listener?.onIsPlayingChanged(isPlaying)
    }

    fun start() {
        val context = context ?: return
        if (videoUrl == null || videoUrl?.isBlank() == true) return

        val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl), shouldCache)

        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource, true, false)
    }

    fun stop() {
        autoStopTimer?.cancel()
        exoPlayer.stop()
    }

    fun restart() {
        start()
    }

    fun release() {
        try {
            exoPlayer.release()
        } catch (throwable: Throwable) {
        }
    }

    fun getPlayer(): ExoPlayer = exoPlayer

    interface VideoPlayerListener {
        fun onIsPlayingChanged(isPlaying: Boolean)
    }

    private fun getMediaSourceBySource(
        context: Context,
        uri: Uri,
        shouldCache: Boolean,
        duration: Long = DEFAULT_CLIP_DURATION
    ): MediaSource {
        val defaultDataSourceFactory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
        val dataSourceFactory = if (shouldCache) {
            CacheDataSourceFactory(VideoPlayerCache.getInstance(context), defaultDataSourceFactory)
        } else defaultDataSourceFactory
        val mediaSourceFactory = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        val mediaSource = mediaSourceFactory.createMediaSource(uri)
        return if (duration != 0L) {
            ClippingMediaSource(mediaSource, duration)
        } else {
            mediaSource
        }
    }

    private fun configureAutoStop(durationLimit: Int?) {
        if (durationLimit == null) return

        autoStopTimer?.cancel()
        autoStopTimer = createStopTimer(durationLimit)
        autoStopTimer?.start()
    }

    private fun createStopTimer(durationLimit: Int): CountDownTimer {
        return object : CountDownTimer(durationLimit.toLong() * 1000, 1000) {
            override fun onFinish() {
                exoPlayer.stop()
            }

            override fun onTick(millisUntilFinished: Long) {}
        }
    }
}