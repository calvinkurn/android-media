package com.tokopedia.home.beranda.presentation.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException
import com.google.android.exoplayer2.util.Util
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.util.ConnectionUtils
import com.tokopedia.home.util.DimensionUtils
import com.tokopedia.play_common.player.TokopediaPlayManager
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.CoroutineContext

@SuppressWarnings("WeakerAccess")
class TokopediaPlayerHelper(
        private val context: Context,
        private val exoPlayerView: TokopediaPlayView
) :
        ExoPlayerControl,
        ExoPlayerStatus,
        Player.EventListener,
        CoroutineScope {

    private var mPlayer: SimpleExoPlayer? = null

    private var mExoPlayerListener: ExoPlayerListener? = null

    private var mVideosUri: Uri? = null
    private var mResumePosition = C.TIME_UNSET
    private var mResumeWindow = C.INDEX_UNSET
    private var isVideoMuted = false
    private var isRepeatModeOn = false
    private var isResumePlayWhenReady = false
    private var isPlayerPrepared = false
    private var isToPrepareOnResume = true

    /* Master job */
    private val masterJob = Job()

    init {
        init()
    }

    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private fun init(){
    }

    // Player creation and release
    private fun setVideoUrls(url: String) {
        mVideosUri = Uri.parse(url)
    }

    private fun createMediaSource() {
        mVideosUri?.let{
            TokopediaPlayManager.getInstance(context).safePlayVideoWithUri(it,false)
        }
    }

    fun isPlayerNull(): Boolean {
        return mPlayer == null
    }

    private fun updateResumePosition() {
        isResumePlayWhenReady = mPlayer?.playWhenReady ?: false
        mResumeWindow = mPlayer?.currentWindowIndex ?: C.INDEX_UNSET
        mResumePosition = Math.max(0, mPlayer?.contentPosition ?: 0)
    }

    private fun isDeviceHasRequirementAutoPlay(): Boolean{
        return DimensionUtils.getDensityMatrix(context) >= 1.5f
    }

    @SuppressLint("SyntheticAccessor")
    class Builder(context: Context, tokopediaPlayView: TokopediaPlayView) {
        private val mExoPlayerHelper: TokopediaPlayerHelper = TokopediaPlayerHelper(context, tokopediaPlayView)

        fun setVideoUrls(url: String): Builder {
            mExoPlayerHelper.setVideoUrls(url)
            return this
        }

        fun setRepeatModeOn(isOn: Boolean): Builder {
            mExoPlayerHelper.isRepeatModeOn = isOn
            return this
        }

        fun setExoPlayerEventsListener(exoPlayerListener: ExoPlayerListener): Builder {
            mExoPlayerHelper.setExoPlayerEventsListener(exoPlayerListener)
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
            mExoPlayerHelper.createPlayer()
            return mExoPlayerHelper
        }
    }

    override fun createPlayer() {
        if (mPlayer != null) {
            return
        }
        mPlayer = TokopediaPlayManager.getInstance(context).videoPlayer as SimpleExoPlayer
        exoPlayerView.setPlayer(mPlayer)
        isVideoMuted = true
        mPlayer?.volume = 0f

        mPlayer?.repeatMode = if(isRepeatModeOn) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
        mPlayer?.playWhenReady = false
        mPlayer?.addListener(this)

        isPlayerPrepared = false

        preparePlayer()
    }

    override fun preparePlayer() {
        if (mPlayer == null || isPlayerPrepared) {
            return
        }
        createMediaSource()
        isPlayerPrepared = true

        if (mResumeWindow != C.INDEX_UNSET) {
            mPlayer?.playWhenReady = isResumePlayWhenReady
            mPlayer?.seekTo(mResumeWindow, mResumePosition + 100)
        } else {
            playerPlay()
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (mPlayer == null) {
            return
        }
        when (playbackState) {
            Player.STATE_READY -> if (playWhenReady) {
                mExoPlayerListener?.onPlayerPlaying(mPlayer?.currentWindowIndex ?: -1)
            } else {
                mExoPlayerListener?.onPlayerPaused(mPlayer?.currentWindowIndex ?: -1)
            }
            Player.STATE_BUFFERING -> {
                mExoPlayerListener?.onPlayerBuffering(mPlayer?.currentWindowIndex ?: -1)
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
                    when {
                        decoderInitializationException.cause is DecoderQueryException -> {
                            context.getString(R.string.error_querying_decoders)
                        }
                        decoderInitializationException.secureDecoderRequired -> {
                            context.getString(R.string.error_no_secure_decoder,
                                    decoderInitializationException.mimeType)
                        }
                        else -> {
                            context.getString(R.string.error_no_decoder,
                                    decoderInitializationException.mimeType)
                        }
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

        if (mExoPlayerListener != null) {
            mExoPlayerListener?.onPlayerError(errorString)
        }
    }

    override fun releasePlayer() {
        masterJob.cancelChildren()
        isPlayerPrepared = false
        mExoPlayerListener?.releaseExoPlayerCalled()
        if (mPlayer != null) {
            updateResumePosition()
            mPlayer?.removeListener(this)
            mPlayer = null
        }
        TokopediaPlayManager.getInstance(context).releasePlayer()
    }

    override fun playerPause() {
        masterJob.cancelChildren()
        updateResumePosition()
        mPlayer?.playWhenReady = false
    }

    override fun playerPlay() {
        if(ConnectionUtils.isWifiConnected(context) && isDeviceHasRequirementAutoPlay() && mPlayer?.isPlaying == false){
            masterJob.cancelChildren()
            launch(coroutineContext){
                delay(3000)
                TokopediaPlayManager.getInstance(context).resumeCurrentVideo()
            }
        }
    }

    override fun seekTo(windowIndex: Int, positionMs: Long) {
        mPlayer?.seekTo(windowIndex, positionMs)
    }

    override fun seekToDefaultPosition() {
        mPlayer?.seekToDefaultPosition()
    }

    override fun setExoPlayerEventsListener(pExoPlayerListenerListener: ExoPlayerListener?) {
        mExoPlayerListener = pExoPlayerListenerListener
    }

    override fun onActivityStart() {
        if (Util.SDK_INT >= 21) {
            createPlayer()
        }
    }

    override fun onActivityResume() {
        TokopediaPlayManager.getInstance(context).stopPlayer()
        mPlayer = TokopediaPlayManager.getInstance(context).videoPlayer as SimpleExoPlayer
        exoPlayerView.setPlayer(mPlayer)
        mPlayer?.addListener(this)
        createMediaSource()
        playerPlay()
    }

    override fun onActivityPause() {
        updateResumePosition()
        mPlayer?.removeListener(this)
        exoPlayerView.setPlayer(null)
        mPlayer = null
    }

    override fun onActivityStop() {
        releasePlayer()
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