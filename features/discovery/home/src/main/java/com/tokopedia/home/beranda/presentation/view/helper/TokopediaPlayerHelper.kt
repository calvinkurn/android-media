package com.tokopedia.home.beranda.presentation.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.util.ConnectionUtils
import com.tokopedia.home.util.DimensionUtils
import com.tokopedia.play_common.player.TokopediaPlayManager
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@SuppressWarnings("WeakerAccess")
class TokopediaPlayerHelper(
        private val context: Context,
        private val exoPlayerView: TokopediaPlayView
) :
        ExoPlayerControl,
        Player.EventListener,
        CoroutineScope {

    private var mPlayer: ExoPlayer? = null

    private var mExoPlayerListener: ExoPlayerListener? = null

    private var videosUri: Uri? = null
    private var mResumePosition: Long = 1
    private var isPlayerPrepared = false

    /**
     * DO NOT CHANGE THIS TO LAMBDA
     */
    private val playerObserver = object: Observer<ExoPlayer> {
        override fun onChanged(t: ExoPlayer?) {
            mPlayer = t
        }
    }

    /* Master job */
    private val masterJob = Job()

    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    // Player creation and release
    private fun setVideoUrl(url: String) {
        videosUri = Uri.parse(url)
    }

    private fun setDataSource() {
        videosUri?.let{
            TokopediaPlayManager.getInstance(context).safePlayVideoWithUri(it,false)
        }
    }

    private fun updateResumePosition() {
        val currentPosition = (TokopediaPlayManager.getInstance(context).videoPlayer as SimpleExoPlayer).currentPosition
        mResumePosition = if(currentPosition == -1L) 1 else currentPosition
    }

    private fun isDeviceHasRequirementAutoPlay(): Boolean{
        return DimensionUtils.getDensityMatrix(context) >= 1.5f
    }

    @SuppressLint("SyntheticAccessor")
    class Builder(context: Context, tokopediaPlayView: TokopediaPlayView) {
        private val mExoPlayerHelper: TokopediaPlayerHelper = TokopediaPlayerHelper(context, tokopediaPlayView)

        fun setVideoUrls(url: String): Builder {
            mExoPlayerHelper.setVideoUrl(url)
            return this
        }

        fun setExoPlayerEventsListener(exoPlayerListener: ExoPlayerListener): Builder {
            mExoPlayerHelper.setExoPlayerEventsListener(exoPlayerListener)
            return this
        }

        fun create(): TokopediaPlayerHelper {
            mExoPlayerHelper.createPlayer()
            return mExoPlayerHelper
        }
    }


    override fun isPlayerNull(): Boolean {
        return mPlayer == null
    }

    override fun createPlayer() {
        if (mPlayer == null) {
            clear()
            mPlayer = TokopediaPlayManager.getInstance(context).videoPlayer as SimpleExoPlayer
            muteVideoPlayer()
            mPlayer?.repeatMode = Player.REPEAT_MODE_ALL
            mPlayer?.playWhenReady = false
            mPlayer?.addListener(this)
            exoPlayerView.setPlayer(mPlayer)
            isPlayerPrepared = false

            preparePlayer()
        }
    }

    override fun preparePlayer() {
        if (mPlayer != null) {
            setDataSource()
            if(mPlayer?.isPlaying != true) playerPlay()
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

    override fun releasePlayer() {
        masterJob.cancelChildren()
        isPlayerPrepared = false
        exoPlayerView.setPlayer(null)
        mExoPlayerListener?.releaseExoPlayerCalled()
        if (mPlayer != null) {
            mPlayer?.removeListener(this)
            mPlayer = null
        }
        TokopediaPlayManager.getInstance(context).releasePlayer()
    }

    override fun playerPause() {
        masterJob.cancelChildren()
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

    override fun seekToDefaultPosition() {
        mPlayer?.seekTo(mResumePosition)
    }

    override fun setExoPlayerEventsListener(pExoPlayerListenerListener: ExoPlayerListener?) {
        mExoPlayerListener = pExoPlayerListenerListener
    }

    override fun onActivityStart() {
        createPlayer()
    }

    override fun onActivityResume() {
        launch(coroutineContext){
            updateResumePosition()
            delay(500)
            observeVideoPlayer()
            stopVideoPlayer()
            mPlayer = TokopediaPlayManager.getInstance(context).videoPlayer as SimpleExoPlayer
            muteVideoPlayer()
            exoPlayerView.setPlayer(mPlayer)
            mPlayer?.addListener(this@TokopediaPlayerHelper)
            setDataSource()
            withContext(Dispatchers.Main) {
                mPlayer?.seekTo(mResumePosition)
            }
            delay(2500)
            TokopediaPlayManager.getInstance(context).resumeCurrentVideo()
        }
    }

    override fun onActivityPause() {
        clear()
    }

    override fun onActivityStop() {
        releasePlayer()
    }

    private fun clear(){
        masterJob.cancelChildren()
        exoPlayerView.setPlayer(null)
        mPlayer?.removeListener(this)
        mPlayer = null
        removeVideoPlayerObserver()
    }

    private fun observeVideoPlayer() {
        TokopediaPlayManager.getInstance(context).getObservableVideoPlayer().observeForever(playerObserver)
    }

    private fun removeVideoPlayerObserver() {
        TokopediaPlayManager.getInstance(context).getObservableVideoPlayer().removeObserver(playerObserver)
    }

    private fun muteVideoPlayer() {
        TokopediaPlayManager.getInstance(context).muteVideo(true)
    }

    private fun stopVideoPlayer() {
        TokopediaPlayManager.getInstance(context).stopPlayer()
    }

}