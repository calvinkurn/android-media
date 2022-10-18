package com.tokopedia.video_widget.carousel

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
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
import com.tokopedia.video_widget.util.DimensionUtils
import com.tokopedia.video_widget.util.SimpleExoPlayerUtils
import java.lang.ref.WeakReference

class CarouselVideoPlayer(context: Context) {
    companion object {
        private const val MINIMUM_DENSITY_MATRIX = 1.5f

        private const val DEFAULT_CLIP_DURATION = 5_000_000L // 5 second in microsecond
        private const val ONE_MICRO_SECOND = 1_000_000L // 1 second in microsecond
    }

    private val contextReference = WeakReference(context)
    private val context: Context?
        get() = contextReference.get()

    var listener: VideoPlayerListener? = null
    var videoUrl: String? = null
    var shouldCache: Boolean = false

    private var maxDuration: Long = DEFAULT_CLIP_DURATION

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> if (DeviceConnectionInfo.isConnectCellular(context)) whenIsPlayingChanged(
                    isPlaying = false
                )
                Player.STATE_READY -> whenIsPlayingChanged(isPlaying = true)
                else -> whenIsPlayingChanged(isPlaying = false)
            }
        }
    }

    private var exoPlayer: ExoPlayer? = SimpleExoPlayerUtils.create(
        context,
        playerEventListener,
        Player.REPEAT_MODE_ONE,
    )

    private fun whenIsPlayingChanged(isPlaying: Boolean) {
        listener?.onIsPlayingChanged(isPlaying)
    }

    fun start() {
        val context = context ?: return
        val exoPlayer = exoPlayer ?: return
        if (videoUrl == null || videoUrl?.isBlank() == true) return
        if (!canPlay(context)) return

        val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl), shouldCache)

        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource, true, false)
    }

    private fun canPlay(context: Context): Boolean {
        return DeviceConnectionInfo.isConnectWifi(context)
            && isDeviceHasRequirementAutoPlay()
    }

    private fun isDeviceHasRequirementAutoPlay(): Boolean {
        val context = context ?: return false
        return DimensionUtils.getDensityMatrix(context) >= MINIMUM_DENSITY_MATRIX
    }

    fun stop() {
        exoPlayer?.stop()
    }

    fun restart() {
        start()
    }

    fun release() {
        try {
            exoPlayer?.release()
        } catch (throwable: Throwable) {
        }
    }

    fun setMaxDurationInMicroSecond(duration: Long) {
        val newDuration = if (duration > 0L) duration else 0L
        this.maxDuration = newDuration
    }

    fun setMaxDurationInSecond(duration: Int) {
        val newDuration = if (duration > 0) duration else 0
        this.maxDuration = newDuration * ONE_MICRO_SECOND
    }

    fun getPlayer(): ExoPlayer? = exoPlayer

    interface VideoPlayerListener {
        fun onIsPlayingChanged(isPlaying: Boolean)
    }

    private fun getMediaSourceBySource(
        context: Context,
        uri: Uri,
        shouldCache: Boolean
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
        return if (maxDuration != 0L) {
            ClippingMediaSource(mediaSource, maxDuration)
        } else {
            mediaSource
        }
    }
}
