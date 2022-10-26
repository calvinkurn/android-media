package com.tokopedia.video_widget

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.video_widget.util.DimensionUtils

class VideoPlayerViewHelper(
    private var exoPlayerView: VideoPlayerView?
) : ExoPlayerControl {
    companion object {
        private const val MINIMUM_DENSITY_MATRIX = 1.5f
        private const val DEFAULT_CLIP_DURATION = 5_000_000L // 5 second in microsecond
    }

    private val context: Context?
        get() = exoPlayerView?.context

    var isAutoplay = true
    var shouldCache = true

    private var exoPlayer: ExoPlayer? = null

    private var videoUri: Uri? = null

    fun setExoPlayer(exoPlayer: ExoPlayer?) {
        this.exoPlayer = exoPlayer
    }

    fun removeExoPlayer() {
        exoPlayer = null
    }

    override fun init() {
    }

    override fun releasePlayer() {
        exoPlayer?.release()
    }

    override fun playerPause() {
        exoPlayerView?.setPlayer(null)
    }

    override fun play(url: String) {
        val context = context ?: return
        if (canPlay(context, url)) {
            videoUri = Uri.parse(url)
            resumeVideo()
        } else {
            playerPause()
        }
    }

    private fun canPlay(context: Context, url: String) : Boolean {
        return DeviceConnectionInfo.isConnectWifi(context) &&
                isDeviceHasRequirementAutoPlay() &&
                (!isPlayerPlaying() || url != videoUri.toString())
    }

    override fun stop() {
        stopVideoPlayer()
        resetPlayer()
    }

    override fun preparePlayer() {
        val context = context ?: return
        if (DeviceConnectionInfo.isConnectWifi(context)) resumeVideo()
    }

    private fun resumeVideo() {
        if (canResume()) {
            playUri(videoUri ?: Uri.parse(""))
            exoPlayerView?.setPlayer(exoPlayer)
        }
    }

    private fun canResume() : Boolean {
        return videoUri != null
                && !videoUri?.toString().isNullOrEmpty()
                && isAutoplay
    }

    private fun playUri(uri: Uri, autoPlay: Boolean = true) {
        if (uri.toString().isEmpty()) {
            releasePlayer()
            return
        }

        playVideoWithUri(uri, autoPlay)

        if (exoPlayer?.isPlaying == false && autoPlay) resumePlayer()
    }

    private fun playVideoWithUri(
        uri: Uri,
        autoPlay: Boolean = true,
        lastPosition: Long? = null,
        resetState: Boolean = true
    ) {
        val context = context ?: return
        val videoPlayer = exoPlayer ?: return
        val mediaSource = getMediaSourceByUri(context, uri)
        videoPlayer.playWhenReady = autoPlay
        videoPlayer.prepare(mediaSource, resetState, resetState)
        if (lastPosition == null) videoPlayer.seekToDefaultPosition()
    }

    private fun getMediaSourceByUri(
        context: Context,
        uri: Uri,
        duration: Long = DEFAULT_CLIP_DURATION
    ): MediaSource {
        val dataSourceFactory = getDataSourceFactory(context)
        val mediaSourceFactory = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        val mediaSource = mediaSourceFactory.createMediaSource(uri)
        return ClippingMediaSource(mediaSource, duration)
    }

    private fun getDataSourceFactory(context: Context) : DataSource.Factory{
        val defaultDataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "Tokopedia Android")
        )
        return if(shouldCache) {
            CacheDataSourceFactory(VideoPlayerCache.getInstance(context), defaultDataSourceFactory)
        } else {
            defaultDataSourceFactory
        }
    }

    private fun resumePlayer() {
        val videoPlayer = exoPlayer ?: return
        if (videoPlayer.playbackState == ExoPlayer.STATE_ENDED) resetPlayer()
        videoPlayer.playWhenReady = true
    }

    private fun resetPlayer() {
        exoPlayer?.seekTo(0)
    }

    override fun isPlayerPlaying(): Boolean {
        return exoPlayer?.isPlaying ?: false
    }

    override fun onViewAttach() {
        preparePlayer()
    }

    override fun onViewDetach() {
        playerPause()
    }

    private fun onActivityPause(shouldPausePlay: Boolean) {
        playerPause()
        if(shouldPausePlay) stopVideoPlayer()
    }

    fun onActivityDestroy() {
        exoPlayerView?.setPlayer(null)
        releasePlayer()
        exoPlayerView = null
        exoPlayer = null
    }

    private fun stopVideoPlayer() {
        exoPlayer?.stop()
    }

    private fun isDeviceHasRequirementAutoPlay(): Boolean {
        val context = context ?: return false
        return DimensionUtils.getDensityMatrix(context) >= MINIMUM_DENSITY_MATRIX
    }
}
