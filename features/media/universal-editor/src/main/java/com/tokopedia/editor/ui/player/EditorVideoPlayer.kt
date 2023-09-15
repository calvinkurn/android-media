package com.tokopedia.editor.ui.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class EditorVideoPlayer constructor(
    private val context: Context
) {

    var videoUrl = ""

    var listener: Listener? = null

    private val exoPlayer = SimpleExoPlayer
        .Builder(context)
        .build()

    private var loopingMediaSource: LoopingMediaSource? = null

    init {
        exoPlayer.addListener(object : Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                listener?.isPlayingOnChanged(isPlaying)
            }
        })
    }

    fun player() = exoPlayer

    fun start() {
        if (videoUrl.isEmpty()) return
        if (exoPlayer.isPlaying) return

        if (loopingMediaSource == null) {
            loopingMediaSource = LoopingMediaSource(getOrCreateMediaSource(Uri.parse(videoUrl)))
        }

        loopingMediaSource?.let {
            exoPlayer.playWhenReady = true
            exoPlayer.prepare(it, true, false)
        }
    }

    fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun resume() {
        exoPlayer.playWhenReady = true
    }

    fun muteAudio(isMute: Boolean) {
        val value = if (isMute) 0 else 1
        exoPlayer.volume = value.toFloat()
    }

    fun release() {
        try {
            exoPlayer.release()
        } catch (ignored: Throwable) {
        }
    }

    private fun getOrCreateMediaSource(uri: Uri): MediaSource {
        val userAgent = Util.getUserAgent(context, "Tokopedia")
        val dataSourceFactory = DefaultDataSourceFactory(context, userAgent)
        val mediaSourceFactory = generateMediaSourceFactory(uri, dataSourceFactory)
        return mediaSourceFactory.createMediaSource(uri)
    }

    private fun generateMediaSourceFactory(
        uri: Uri,
        dsFactory: DataSource.Factory
    ): MediaSourceFactory {
        return when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dsFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dsFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dsFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dsFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    interface Listener {
        fun isPlayingOnChanged(isPlaying: Boolean)
    }

}
