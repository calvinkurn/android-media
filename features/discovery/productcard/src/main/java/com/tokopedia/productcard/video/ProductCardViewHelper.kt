package com.tokopedia.productcard.video

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
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
import com.tokopedia.productcard.ProductCardVideoView
import com.tokopedia.productcard.utils.DimensionUtils
import java.lang.ref.WeakReference

class ProductCardViewHelper(
    context: Context,
    private val exoPlayerView: ProductCardVideoView
) : ExoPlayerControl {
    companion object {
        private const val MINIMUM_DENSITY_MATRIX = 1.5f
        private const val DEFAULT_CLIP_DURATION = 5_000_000L // 5 second in microsecond
    }

    private val contextReference: WeakReference<Context> = WeakReference(context)
    private val context: Context?
        get() = contextReference.get()

    var isAutoPlay = true
    var shouldCache = true

    private var videoPlayer: ExoPlayer? = null
    private var mExoPlayerListener: ExoPlayerListener? = null

    private var videoUri: Uri? = null

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            val exoPlayerListener = mExoPlayerListener ?: return
            when (playbackState) {
                Player.STATE_IDLE -> exoPlayerListener.onPlayerIdle()
                Player.STATE_BUFFERING -> exoPlayerListener.onPlayerBuffering()
                Player.STATE_READY -> {
                    if (!playWhenReady) {
                        exoPlayerListener.onPlayerPaused()
                    } else {
                        exoPlayerListener.onPlayerPlaying()
                    }
                }
                Player.STATE_ENDED -> {
                    exoPlayerListener.onPlayerEnded()
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            val exoPlayerListener = mExoPlayerListener ?: return
            exoPlayerListener.onPlayerError(error.toString())
        }
    }

    private fun initVideoPlayer(context: Context) {
        videoPlayer = SimpleExoPlayer.Builder(context)
            .setLoadControl(DefaultLoadControl())
            .build()
            .apply {
                addListener(playerEventListener)
                setAudioAttributes(initAudioAttributes(), true)
            }
            .also {
                mute(it)
                setRepeatMode(it)
            }
    }

    /**
     * To handle audio focus and content type
     */
    private fun initAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }

    private fun mute(videoPlayer: SimpleExoPlayer) = synchronized(this) {
        videoPlayer.volume = 0f
    }

    private fun setRepeatMode(videoPlayer: SimpleExoPlayer) = synchronized(this) {
        videoPlayer.repeatMode = Player.REPEAT_MODE_OFF
    }

    class Builder(context: Context, productCardVideoView: ProductCardVideoView) {
        private val mExoPlayerHelper: ProductCardViewHelper = ProductCardViewHelper(
            context,
            productCardVideoView
        )

        fun setExoPlayerEventsListener(exoPlayerListener: ExoPlayerListener): Builder {
            mExoPlayerHelper.setExoPlayerEventsListener(exoPlayerListener)
            return this
        }

        fun create(): ProductCardViewHelper {
            mExoPlayerHelper.init()
            return mExoPlayerHelper
        }
    }

    override fun init() {
        val context = context?: return
        initVideoPlayer(context)
    }

    override fun releasePlayer() {
        videoPlayer?.release()
    }

    override fun playerPause() {
        exoPlayerView.setPlayer(null)
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
            exoPlayerView.setPlayer(videoPlayer)
        }
    }

    private fun canResume() : Boolean {
        return videoUri != null
                && !videoUri?.toString().isNullOrEmpty()
                && isAutoPlay
    }

    private fun playUri(uri: Uri, autoPlay: Boolean = true) {
        if (uri.toString().isEmpty()) {
            releasePlayer()
            return
        }

        playVideoWithUri(uri, autoPlay)

        if (videoPlayer?.isPlaying == false && autoPlay) resumePlayer()
    }

    private fun playVideoWithUri(
        uri: Uri,
        autoPlay: Boolean = true,
        lastPosition: Long? = null,
        resetState: Boolean = true
    ) {
        val context = context ?: return
        val videoPlayer = videoPlayer ?: return
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
            CacheDataSourceFactory(ProductVideoCache.getInstance(context), defaultDataSourceFactory)
        } else {
            defaultDataSourceFactory
        }
    }

    private fun resumePlayer() {
        val videoPlayer = videoPlayer?: return
        if (videoPlayer.playbackState == ExoPlayer.STATE_ENDED) resetPlayer()
        videoPlayer.playWhenReady = true
    }

    private fun resetPlayer() {
        videoPlayer?.seekTo(0)
    }

    override fun isPlayerPlaying(): Boolean {
        return videoPlayer?.isPlaying ?: false
    }

    override fun onViewAttach() {
        preparePlayer()
    }

    override fun onViewDetach() {
        playerPause()
    }

    override fun setExoPlayerEventsListener(exoPlayerListener: ExoPlayerListener?) {
        mExoPlayerListener = exoPlayerListener
    }

    private fun onActivityPause(shouldPausePlay: Boolean) {
        playerPause()
        if(shouldPausePlay) stopVideoPlayer()
    }

    private  fun onActivityDestroy() {
        exoPlayerView.setPlayer(null)
        releasePlayer()
        videoPlayer = null
    }

    private fun stopVideoPlayer() {
        videoPlayer?.stop()
    }

    private fun isDeviceHasRequirementAutoPlay(): Boolean {
        val context = context ?: return false
        return DimensionUtils.getDensityMatrix(context) >= MINIMUM_DENSITY_MATRIX
    }
}