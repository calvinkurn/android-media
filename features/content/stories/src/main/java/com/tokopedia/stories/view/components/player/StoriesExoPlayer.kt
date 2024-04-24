package com.tokopedia.stories.view.components.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import java.util.concurrent.TimeUnit

/**
 * Created By : Muhammad Furqan on 17/10/23
 */
class StoriesExoPlayer(val context: Context) {

    private val loadControl = DefaultLoadControl.Builder()
        .setBackBuffer(
            TimeUnit.MINUTES.toMillis(1).toInt(),
            true
        )
        .setBufferDurationsMs(
            TimeUnit.SECONDS.toMillis(30).toInt(),
            DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
            TimeUnit.SECONDS.toMillis(1).toInt(),
            TimeUnit.SECONDS.toMillis(1).toInt()
        ).createDefaultLoadControl()

    private val _exoPlayer = SimpleExoPlayer.Builder(context)
        .setTrackSelector(DefaultTrackSelector(context))
        .setLoadControl(loadControl)
        .build()
    val exoPlayer: SimpleExoPlayer
        get() = _exoPlayer

    private var groupId: String = ""

    init {
        exoPlayer.volume = UNMUTE_VOLUME
        exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    }

    fun setEventListener(listener: Player.EventListener) {
        exoPlayer.removeListener(listener)
        exoPlayer.addListener(listener)
    }

    fun setVideoListener(listener: VideoListener) {
        exoPlayer.removeVideoListener(listener)
        exoPlayer.addVideoListener(listener)
    }

    fun start(videoUrl: String, groupId: String, isAutoPlay: Boolean) {
        if (videoUrl.isEmpty()) return

        this.groupId = groupId

        val mediaSource = getMediaSourceByUri(context, Uri.parse(videoUrl))
        exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
        exoPlayer.playWhenReady = isAutoPlay
        exoPlayer.prepare(mediaSource, true, false)
    }

    fun resume(shouldReset: Boolean = false, activeGroupId: String) {
        if (groupId != activeGroupId) {
            return
        }

        exoPlayer.playWhenReady = true
        if (shouldReset) {
            exoPlayer.seekTo(0)
        }
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
    }

    fun destroy() {
        exoPlayer.release()
    }

    private fun getMediaSourceByUri(
        context: Context,
        uri: Uri
    ): MediaSource {
        val mDataSourceFactory = getDataSourceFactory(context)

        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(mDataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(mDataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(mDataSourceFactory)
                .setAllowChunklessPreparation(true)

            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(mDataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }

    private fun getDataSourceFactory(context: Context): DataSource.Factory {
        return DefaultHttpDataSourceFactory(
            Util.getUserAgent(context, "Tokopedia Android")
        )
    }

    companion object {
        private const val UNMUTE_VOLUME = 1f
    }
}
