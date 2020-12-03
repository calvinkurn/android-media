package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.net.Uri
import android.net.wifi.WifiManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener


/**
 * Created by Yehezkiel on 23/11/20
 * ExoPlayer need to re-prepare if video url is changed, and this causing loading time when first open video.
 * So we only prepare when needed, if next destination is also video type which means we need to change url. So we prepare it
 * If next destination is not video type (image type) just pause the video.
 */
class ProductExoPlayer(val context: Context) {

    private var videoStateListener: VideoStateListener? = null

    private var loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, 16))
            .setBufferDurationsMs(MIN_BUFFER_DURATION,
                    MAX_BUFFER_DURATION,
                    MIN_PLAYBACK_START_BUFFER,
                    MIN_PLAYBACK_RESUME_BUFFER)
            .setTargetBufferBytes(-1)
            .setPrioritizeTimeOverSizeThresholds(true)
            .createDefaultLoadControl()

    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context))
            .setLoadControl(loadControl)
            .build()

    private val wifi: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    init {
        exoPlayer.volume = 0F
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val isPlaying = playWhenReady && playbackState == Player.STATE_READY
                val isReadyToPlay = !playWhenReady && playbackState == Player.STATE_READY
                val isInitialLoad = (playWhenReady && playbackState != Player.STATE_READY) || (!playWhenReady && playbackState != Player.STATE_READY)

                when {
                    isPlaying || playbackState == Player.STATE_ENDED -> {
                        videoStateListener?.onVideoReadyToPlay()
                    }
                    isReadyToPlay -> {
                        videoStateListener?.onVideoReadyToPlay()
                    }
                    isInitialLoad -> {
                        videoStateListener?.onInitialStateLoading()
                    }
                    else -> videoStateListener?.onVideoBuffering()
                }
            }
        })
    }

    fun setVideoResizeListener(listener: VideoListener) {
        exoPlayer.addVideoListener(listener)
    }

    fun setVideoStateListener(videoStateListener: VideoStateListener?) {
        this.videoStateListener = videoStateListener
    }

    /**
     * @shouldPrepare
     * True if last video is different with current video
     * False if last video is same with current video (means we open the same video as before)
     */
    fun start(videoUrl: String, lastVideoPosition: Long, isMute: Boolean, shouldPrepare: Boolean = true) {
        if (videoUrl.isBlank()) return

        val mediaSource = getMediaSourceBySource(context, Uri.parse(videoUrl))

        if (lastVideoPosition != 0L && shouldPrepare) {
            exoPlayer.seekTo(lastVideoPosition)
        }
        toggleVideoVolume(isMute)
        exoPlayer.playWhenReady = isConnectedToWifi()
        prepareIfVideoDifferent(mediaSource, lastVideoPosition, shouldPrepare)
    }

    fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun resumeAndSeekTo(lastVideoPosition: Long) {
        if (lastVideoPosition != 0L) {
            exoPlayer.seekTo(lastVideoPosition)
        }
        exoPlayer.playWhenReady = false
    }

    fun destroy() {
        exoPlayer.release()
    }

    fun toggleVideoVolume(isMute: Boolean) {
        if (isMute) {
            exoPlayer.volume = 0F
        } else {
            exoPlayer.volume = 1F
        }
        videoStateListener?.configureVolume(isMute)
    }

    fun getExoPlayer(): SimpleExoPlayer = exoPlayer

    fun isMute(): Boolean = exoPlayer.volume == 0F

    private fun prepareIfVideoDifferent(mediaSource: MediaSource, lastVideoPosition: Long, shouldPrepare: Boolean) {
        if (shouldPrepare) exoPlayer.prepare(mediaSource, lastVideoPosition == 0L, false)
    }

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

    private fun isConnectedToWifi(): Boolean {
        if (wifi.isWifiEnabled) {
            val wifiInfo = wifi.connectionInfo

            if (wifiInfo.networkId == -1) {
                return false
            }

            return true
        } else {
            return false
        }
    }

    companion object {
        //Minimum Video you want to buffer while Playing
        private const val MIN_BUFFER_DURATION = 2000

        //Max Video you want to buffer during PlayBack
        private const val MAX_BUFFER_DURATION = 5000

        //Min Video you want to buffer before start Playing it
        private const val MIN_PLAYBACK_START_BUFFER = 1500

        //Min video You want to buffer when user resumes video
        private const val MIN_PLAYBACK_RESUME_BUFFER = 2000
    }
}

interface VideoStateListener {
    fun onInitialStateLoading()
    fun onVideoReadyToPlay()
    fun onVideoBuffering()
    fun configureVolume(isMute: Boolean)
}