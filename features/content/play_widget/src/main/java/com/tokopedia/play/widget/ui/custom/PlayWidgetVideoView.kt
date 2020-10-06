package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


/**
 * Created by mzennis on 06/10/20.
 */
class PlayWidgetVideoView(context: Context, attrs: AttributeSet?) : PlayerView(context, attrs) {

    private val exoPlayer: SimpleExoPlayer
    private var state: PlayWidgetVideoState = PlayWidgetVideoState.NotReady

    var listener: PlayWidgetVideoListener? = null
    var videoUrl: String = ""

    init {
        this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        this.useController = false

        exoPlayer = SimpleExoPlayer.Builder(context).build()
        exoPlayer.volume = 0F
        exoPlayer.addListener(object : Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> exoPlayer.seekTo(0)
                    Player.STATE_READY -> state = PlayWidgetVideoState.Ready
                    else -> state = PlayWidgetVideoState.NotReady
                }
                listener?.onPlayerStateChanged(state)
            }
        })
        this.player = exoPlayer
    }

    fun start() {
        if (videoUrl.isEmpty() || videoUrl.isBlank()) return

        val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl))
        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource,true, false)
    }

    fun stop() {
        exoPlayer.stop()
    }

    fun release() {
        try {
            exoPlayer.release()
        } catch (throwable: Throwable) {
        }
    }

    fun isPlaying(): Boolean = state == PlayWidgetVideoState.Ready

    enum class PlayWidgetVideoState { Ready, NotReady }

    interface PlayWidgetVideoListener {
        fun onPlayerStateChanged(state: PlayWidgetVideoState)
    }

    private fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }
}