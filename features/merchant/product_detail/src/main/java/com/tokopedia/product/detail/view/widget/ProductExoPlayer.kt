package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


/**
 * Created by Yehezkiel on 23/11/20
 */
class ProductExoPlayer(val context: Context) {

    companion object {
        //Minimum Video you want to buffer while Playing
        private const val MIN_BUFFER_DURATION = 32*1024

        //Max Video you want to buffer during PlayBack
        private const val MAX_BUFFER_DURATION = 64*1024

        //Min Video you want to buffer before start Playing it
        private const val MIN_PLAYBACK_START_BUFFER = 1024

        //Min video You want to buffer when user resumes video
        private const val MIN_PLAYBACK_RESUME_BUFFER = 1024
    }

    private var loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, 16))
            .setBufferDurationsMs(MIN_BUFFER_DURATION,
                    MAX_BUFFER_DURATION,
                    MIN_PLAYBACK_START_BUFFER,
                    MIN_PLAYBACK_RESUME_BUFFER)
            .setTargetBufferBytes(-1)
            .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl()


    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context))
            .setLoadControl(loadControl)
            .build()

    fun start(videoUrl: String, lastVideoPosition: Long) {
        if (videoUrl.isBlank()) return

        val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl))

        if (lastVideoPosition != 0L) {
            exoPlayer.seekTo(lastVideoPosition)
        }
        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource, lastVideoPosition == 0L, false)
    }

    fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun destroy() {
        exoPlayer.release()
    }

    fun getExoPlayer(): SimpleExoPlayer = exoPlayer

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
}