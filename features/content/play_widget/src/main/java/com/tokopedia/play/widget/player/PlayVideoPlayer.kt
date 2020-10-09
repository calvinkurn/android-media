package com.tokopedia.play.widget.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.*


/**
 * Created by mzennis on 09/10/20.
 */
open class PlayVideoPlayer(val context: Context) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
    private var playerState: VideoPlayerState = VideoPlayerState.Idle

    var listener: VideoPlayerListener? = null
    var videoUrl: String? = null

    init {
        exoPlayer.volume = 0F
        exoPlayer.addListener(object : Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> exoPlayer.seekTo(0)
                    Player.STATE_READY -> whenIsPlayingChanged(isPlaying = true)
                    else -> whenIsPlayingChanged(isPlaying = false)
                }
            }
        })
    }

    private fun whenIsPlayingChanged(isPlaying: Boolean) {
        scope.launch(Dispatchers.Main) {
            listener?.onIsPlayingChanged(isPlaying)
        }
    }

    fun start() {
        if (videoUrl?.isBlank() == true) return

        scope.launch {
            val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl))
            exoPlayer.playWhenReady = true
            exoPlayer.prepare(mediaSource,true, false)
        }
    }

    fun stop() {
        scope.launch {
            exoPlayer.stop()
        }
    }

    fun release() {
        try {
            exoPlayer.release()
        } catch (throwable: Throwable) {
        }
        flush()
    }

    fun isIdle(): Boolean = playerState == VideoPlayerState.Idle

    fun isBusy(): Boolean = playerState == VideoPlayerState.Busy

    fun setState(state: VideoPlayerState) {
        this.playerState = state
    }

    fun getPlayerState(): VideoPlayerState = playerState

    fun getPlayer(): ExoPlayer = exoPlayer

    private fun flush() {
        if (scope.isActive && !job.isCancelled){
            job.cancelChildren()
        }
    }

    enum class VideoPlayerState { Idle, Busy }

    interface VideoPlayerListener {
        fun onIsPlayingChanged(isPlaying: Boolean)
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