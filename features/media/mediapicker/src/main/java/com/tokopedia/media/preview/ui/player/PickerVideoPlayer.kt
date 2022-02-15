package com.tokopedia.media.preview.ui.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util

class PickerVideoPlayer constructor(
    private val context: Context
) {

    var videoUrl = ""
    var shouldCache = false

    var listener: Listener? = null

    private val exoPlayer = SimpleExoPlayer
        .Builder(context)
        .build()

    init {
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        listener?.onPlayStateChanged(true)
                    }
                    else -> {
                        listener?.onPlayStateChanged(false)
                    }
                }
            }
        })
    }

    fun player() = exoPlayer

    fun start() {
        if (videoUrl.isEmpty()) return

        val mediaSource = getOrCreateMediaSource(
            Uri.parse(videoUrl),
            shouldCache
        )

        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource, true, false)
    }

    fun stop() {
        exoPlayer.stop()
    }

    fun release() {
        try {
            exoPlayer.release()
        } catch (ignored: Throwable) {}
    }

    private fun getOrCreateMediaSource(uri: Uri, isCache: Boolean): MediaSource {
        val defaultDataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "Tokopedia")
        )

        val dataSourceFactory = if (!isCache) defaultDataSourceFactory else {
            CacheDataSourceFactory(
                PickerPlayerViewCache.get(context),
                defaultDataSourceFactory
            )
        }

        val mediaSourceFactory = when(val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }

        return mediaSourceFactory.createMediaSource(uri)
    }

    interface Listener {
        fun onPlayStateChanged(isPlaying: Boolean)
    }

}