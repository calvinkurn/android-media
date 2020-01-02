package com.tokopedia.home.beranda.presentation.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.util.ConnectionUtils
import com.tokopedia.home.util.DimensionUtils
import com.tokopedia.play_common.player.TokopediaPlayManager
import timber.log.Timber
import java.io.IOException

@SuppressWarnings("WeakerAccess")
class TokopediaPlayerHelper(
        private val context: Context,
        private val exoPlayerView: TokopediaPlayView
) :
        ExoPlayerControl,
        ExoPlayerStatus,
        Player.EventListener {

    private var mPlayer: SimpleExoPlayer? = null
    private var mDataSourceFactory: DataSource.Factory? = null
    private var mLoadControl: DefaultLoadControl? = null
    private var mMediaSource: MediaSource? = null

    private var mProgressBar: ProgressBar? = null

    private var mExoPlayerListener: ExoPlayerListener? = null
    private var mExoThumbListener: ExoThumbListener? = null

    private var mVideosUris: Array<Uri>? = null
    private var mResumePosition = C.TIME_UNSET
    private var mResumeWindow = C.INDEX_UNSET
    private var mTempCurrentVolume = 0f
    private var isVideoMuted = false
    private var isRepeatModeOn = false
    private var isAutoPlayOn = false
    private var isResumePlayWhenReady = false
    private var isPlayerPrepared = false
    private var isToPrepareOnResume = true
    private var isThumbImageViewEnabled = false
    private var mThumbImage: ImageView? = null

    init {
        init()
    }

    private fun init(){
        // Measures bandwidth during playback. Can be null if not required.
        // Measures bandwidth during playback. Can be null if not required.
        val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()

        // Produces DataSource instances through which media data is loaded.
        // Produces DataSource instances through which media data is loaded.
        mDataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "home"), bandwidthMeter)


        // LoadControl that controls when the MediaSource buffers more media, and how much media is buffered.
        // LoadControl is injected when the player is created.
        // removed deprecated DefaultLoadControl creation method
        // LoadControl that controls when the MediaSource buffers more media, and how much media is buffered.
        // LoadControl is injected when the player is created.
        //removed deprecated DefaultLoadControl creation method
        val builder = DefaultLoadControl.Builder()
        builder.setAllocator(DefaultAllocator(true, 2 * 1024 * 1024))
        builder.setBufferDurationsMs(5000, 5000, 5000, 5000)
        builder.setPrioritizeTimeOverSizeThresholds(true)
        mLoadControl = builder.createDefaultLoadControl()
    }

    // Player creation and release
    private fun setVideoUrls(vararg urls: String) {
        mVideosUris = Array(urls.size){
            Uri.parse(urls[it])
        }
    }

    private fun createMediaSource() {
        // A MediaSource defines the media to be played, loads the media, and from which the loaded media can be read.
        // A MediaSource is injected via ExoPlayer.prepare at the start of playback.
        mVideosUris?.let {
            TokopediaPlayManager.getInstance(context).playVideoWithUri(it[0], false)
//            val mediaSources: MutableList<MediaSource> = mutableListOf()
//            it.forEach {uri ->
//                mediaSources.add(buildMediaSource(uri))
//            }
//            mMediaSource = if(mediaSources.size == 1)  mediaSources[0] else ConcatenatingMediaSource(*mediaSources.toTypedArray())
        }
    }

    fun isPlayerNull(): Boolean{
        return mPlayer == null
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        return when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_DASH -> DashMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    private fun setProgressVisible(visible: Boolean) {
        if (mProgressBar != null) {
            mProgressBar?.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun updateResumePosition() {
        isResumePlayWhenReady = mPlayer?.playWhenReady ?: false
        mResumeWindow = mPlayer?.currentWindowIndex ?: C.INDEX_UNSET
        mResumePosition = Math.max(0, mPlayer?.contentPosition ?: 0)
    }

    private fun addThumbImageView() {
        if (mThumbImage == null) {
            val frameLayout: AspectRatioFrameLayout = exoPlayerView.findViewById(R.id.exo_content_frame)
            mThumbImage = ImageView(context)
            mThumbImage?.id = R.id.thumbImg
            val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.CENTER
            mThumbImage?.layoutParams = params
            mThumbImage?.setBackgroundColor(Color.BLACK)
            frameLayout.addView(mThumbImage)
            if (mExoThumbListener != null) {
                mExoThumbListener?.onThumbImageViewReady(mThumbImage!!)
            }
        }
    }

    private fun removeThumbImageView() {
        if (mThumbImage != null) {
            val frameLayout: AspectRatioFrameLayout = exoPlayerView.findViewById(R.id.exo_content_frame)
            frameLayout.removeView(mThumbImage)
            mThumbImage = null
        }
    }

    // Player events, internal handle
    private fun onPlayerBuffering() {

    }

    private fun onPlayerPlaying() {
        removeThumbImageView()
    }

    private fun onPlayerPaused() {
        setProgressVisible(false)
    }

    private fun isDeviceHasRequirementAutoPlay(): Boolean{
        return DimensionUtils.getDensityMatrix(context) >= 1.5f
    }

    @SuppressLint("SyntheticAccessor")
    class Builder(context: Context, tokopediaPlayView: TokopediaPlayView) {
        private val mExoPlayerHelper: TokopediaPlayerHelper = TokopediaPlayerHelper(context, tokopediaPlayView)

        fun setVideoUrls(vararg urls: String): Builder {
            mExoPlayerHelper.setVideoUrls(*urls)
            return this
        }

        fun setRepeatModeOn(isOn: Boolean): Builder {
            mExoPlayerHelper.isRepeatModeOn = isOn
            return this
        }

        fun setMutedVolume(): Builder{
            mExoPlayerHelper.setPlayerMuted()
            return this
        }

        fun setAutoPlayOn(isAutoPlayOn: Boolean): Builder {
            mExoPlayerHelper.isAutoPlayOn = isAutoPlayOn
            return this
        }

        fun setExoPlayerEventsListener(exoPlayerListener: ExoPlayerListener): Builder {
            mExoPlayerHelper.setExoPlayerEventsListener(exoPlayerListener)
            return this
        }

        fun setThumbImageViewEnabled(exoThumbListener: ExoThumbListener?): Builder {
            mExoPlayerHelper.setExoThumbListener(exoThumbListener)
            return this
        }

        /**
         * If you have a list of videos set isToPrepareOnResume to be false
         * to prevent auto prepare on activity onResume/onCreate
         */
        fun setToPrepareOnResume(toPrepareOnResume: Boolean): Builder {
            mExoPlayerHelper.isToPrepareOnResume = toPrepareOnResume
            return this
        }

        /**
         * Probably you will feel a need to use that method when you need to show pre-roll ad
         * and you not interested in auto play. That method allows to separate player creation
         * from calling prepare()
         * Note: To play ad/content you ned to call preparePlayer()
         *
         * @return ExoPlayerHelper instance
         */
        fun create(): TokopediaPlayerHelper {
            mExoPlayerHelper.createPlayer(false)
            return mExoPlayerHelper
        }
    }

    override fun createPlayer(isToPrepare: Boolean) {

        if (mPlayer != null) {
            return
        }

        if (isThumbImageViewEnabled) {
            addThumbImageView()
        }

        TokopediaPlayManager.deleteInstance()
        mPlayer = TokopediaPlayManager.getInstance(context).videoPlayer as SimpleExoPlayer

//        mPlayer = ExoPlayerFactory.newSimpleInstance(
//                context,
//                DefaultRenderersFactory(context),
//                DefaultTrackSelector(),
//                mLoadControl)

        exoPlayerView.setPlayer(mPlayer)
        isVideoMuted = true
        mPlayer?.volume = 0f
        mTempCurrentVolume = mPlayer?.volume ?: 0f

        mPlayer?.repeatMode = if(isRepeatModeOn) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
        mPlayer?.playWhenReady = isAutoPlayOn
        mPlayer?.addListener(this)

        if (isToPrepare) {
            preparePlayer()
        }
    }

    override fun preparePlayer() {
        if (mPlayer == null || isPlayerPrepared) {
            return
        }
        createMediaSource()
        isPlayerPrepared = true
//        mPlayer?.prepare(mMediaSource)

        if (mResumeWindow != C.INDEX_UNSET) {
            mPlayer?.playWhenReady = isResumePlayWhenReady
            mPlayer?.seekTo(mResumeWindow, mResumePosition + 100)
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (mPlayer == null) {
            return
        }
        when (playbackState) {
            Player.STATE_READY -> if (playWhenReady) {
                onPlayerPlaying()
                mExoPlayerListener?.onPlayerPlaying(mPlayer?.currentWindowIndex ?: -1)
            } else {
                onPlayerPaused()
                mExoPlayerListener?.onPlayerPaused(mPlayer?.currentWindowIndex ?: -1)
            }
            Player.STATE_BUFFERING -> {
                onPlayerBuffering()
                mExoPlayerListener?.onPlayerBuffering(mPlayer?.currentWindowIndex ?: -1)
            }
            Player.STATE_ENDED -> {

            }
            Player.STATE_IDLE -> {

            }
            else -> Timber.tag(TokopediaPlayerHelper::class.java.name).e("onPlayerStateChanged unknown: $playbackState")
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        var errorString: String? = null

        when (error.type) {
            ExoPlaybackException.TYPE_SOURCE -> {
                //https://github.com/google/ExoPlayer/issues/2702
                val ex: IOException = error.sourceException
                val msg = ex.message
                if (msg != null) {
                    Timber.tag(TokopediaPlayerHelper::class.java.name).e(msg)
                    errorString = msg
                }
            }
            ExoPlaybackException.TYPE_RENDERER -> {
                val exception: Exception = error.rendererException
                if (exception.message != null) {
                    errorString = exception.message ?: ""
                    Timber.tag(TokopediaPlayerHelper::class.java.name).e(exception)
                }
            }
            ExoPlaybackException.TYPE_UNEXPECTED -> {
                val runtimeException: RuntimeException = error.unexpectedException
                Timber.tag(TokopediaPlayerHelper::class.java.name).e(if (runtimeException.message == null) "Message is null" else runtimeException.message)
                if (runtimeException.message == null) {
                    runtimeException.printStackTrace()
                }
                errorString = runtimeException.message
            }
            ExoPlaybackException.TYPE_OUT_OF_MEMORY -> {
            }
            ExoPlaybackException.TYPE_REMOTE -> {
            }
        }


        if (error.type == ExoPlaybackException.TYPE_RENDERER) {
            val cause: Exception = error.rendererException
            if (cause is DecoderInitializationException) { // Special case for decoder initialization failures.
                val decoderInitializationException = cause
                errorString = if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.cause is DecoderQueryException) {
                        context.getString(R.string.error_querying_decoders)
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        context.getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType)
                    } else {
                        context.getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType)
                    }
                } else {
                    context.getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName)
                }
            }
        }
        if (errorString != null) {
            Timber.tag(TokopediaPlayerHelper::class.java.name).e("$errorString")
        }

        if (isBehindLiveWindow(error)) {
            createPlayer(true)
            Timber.tag(TokopediaPlayerHelper::class.java.name).e("isBehindLiveWindow is true")
        }

        if (mExoPlayerListener != null) {
            mExoPlayerListener?.onPlayerError(errorString)
        }
    }

    private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false
        }
        var cause: Throwable? = e.sourceException
        while (cause != null) {
            if (cause is BehindLiveWindowException) {
                return true
            }
            cause = cause.cause
        }
        return false
    }

    override fun releasePlayer() {
        isPlayerPrepared = false
        mExoPlayerListener?.releaseExoPlayerCalled()
        if (mPlayer != null) {
            updateResumePosition()
            removeThumbImageView()
            mPlayer?.removeListener(this)
            mPlayer?.release()
            mPlayer = null
        }
        Timber.tag("TokopediaPlay").d("Status: Release Player")
    }

    override fun playerPause() {
        updateResumePosition()
        removeThumbImageView()
        mPlayer?.playWhenReady = false
    }

    override fun playerPlay() {
        if(ConnectionUtils.isWifiConnected(context)){
            Thread.sleep(3000)
            mPlayer?.playWhenReady = true
        }
    }

    override fun seekTo(windowIndex: Int, positionMs: Long) {
        mPlayer?.seekTo(windowIndex, positionMs)
    }

    override fun seekToDefaultPosition() {
        mPlayer?.seekToDefaultPosition()
    }

    override fun setExoPlayerEventsListener(pExoPlayerListenerListener: ExoPlayerListener?) {
        mExoPlayerListener = pExoPlayerListenerListener;
    }

    override fun setExoThumbListener(exoThumbListener: ExoThumbListener?) {
        isThumbImageViewEnabled = true
        mExoThumbListener = exoThumbListener
    }

    override fun onActivityStart() {
        if (Util.SDK_INT > 23) {
            createPlayer(isToPrepareOnResume)
        }
    }

    override fun onActivityResume() {
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            createPlayer(isToPrepareOnResume)
        }
    }

    override fun onActivityPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onActivityStop() {
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun updateVideoMuted() {
        isVideoMuted = !isVideoMuted
        if (isVideoMuted) {
            mPlayer?.volume = 0f
        } else {
            mPlayer?.volume = mTempCurrentVolume
        }
    }

    override fun setPlayerMuted() {
        isVideoMuted = true
        mPlayer?.volume = 0f
    }

    override fun isPlayerVideoMuted(): Boolean {
        return isVideoMuted
    }

    override fun currentWindowIndex(): Int {
        return if (mPlayer != null) {
            mPlayer?.currentWindowIndex ?: 0
        } else {
            0
        }
    }

    override fun currentPosition(): Long {
        return if (mPlayer != null) {
            mPlayer?.currentPosition ?: 0
        } else {
            0
        }
    }

    override fun duration(): Long {
        return if (mPlayer != null) {
            mPlayer?.duration ?: 0
        } else {
            0
        }
    }

    override fun isPlayerCreated(): Boolean {
        return mPlayer != null
    }

    override fun isPlayerPrepared(): Boolean {
        return isPlayerPrepared
    }
}