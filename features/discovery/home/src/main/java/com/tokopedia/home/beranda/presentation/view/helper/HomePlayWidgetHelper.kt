package com.tokopedia.home.beranda.presentation.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.util.ConnectionUtils
import com.tokopedia.home.util.DimensionUtils
import com.tokopedia.play_common.player.TokopediaPlayManager
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@SuppressLint("SyntheticAccessor")
@SuppressWarnings("WeakerAccess")
class HomePlayWidgetHelper(
        private val context: Context,
        private val exoPlayerView: TokopediaPlayView
) :
        ExoPlayerControl,
        CoroutineScope {

    private var mPlayer: ExoPlayer? = null
    private var mExoPlayerListener: ExoPlayerListener? = null

    private var videosUri: Uri? = null
    private var mResumePosition: Long = 10

    /**
     * DO NOT CHANGE THIS TO LAMBDA
     */
    private val playerObserver = object : Observer<ExoPlayer> {
        override fun onChanged(t: ExoPlayer?) {
            mPlayer = t
        }
    }

    private val playerStateObserver = object : Observer<TokopediaPlayVideoState>{
        override fun onChanged(state: TokopediaPlayVideoState) {
            when(state){
                is TokopediaPlayVideoState.NoMedia -> mExoPlayerListener?.onPlayerIdle()
                is TokopediaPlayVideoState.Error -> mExoPlayerListener?.onPlayerError(state.error.message)
                is TokopediaPlayVideoState.Pause -> mExoPlayerListener?.onPlayerPaused()
                is TokopediaPlayVideoState.Playing -> mExoPlayerListener?.onPlayerPlaying()
            }
        }
    }

    /* Master job */
    private val masterJob = Job()

    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private fun isDeviceHasRequirementAutoPlay(): Boolean{
        return DimensionUtils.getDensityMatrix(context) >= 1.5f
    }

    @SuppressLint("SyntheticAccessor")
    class Builder(context: Context, tokopediaPlayView: TokopediaPlayView) {
        private val mExoPlayerHelper: HomePlayWidgetHelper = HomePlayWidgetHelper(context, tokopediaPlayView)

        fun setExoPlayerEventsListener(exoPlayerListener: ExoPlayerListener): Builder {
            mExoPlayerHelper.setExoPlayerEventsListener(exoPlayerListener)
            return this
        }

        fun create(): HomePlayWidgetHelper {
            mExoPlayerHelper.init()
            return mExoPlayerHelper
        }
    }

    override fun isPlayerNull(): Boolean {
        return mPlayer == null
    }

    override fun init() {
        observeVideoPlayer()
        muteVideoPlayer()
    }

    override fun createPlayer() {
        if (mPlayer == null) {
            exoPlayerView.setPlayer(mPlayer)
        }
    }

    override fun releasePlayer() {
        masterJob.cancelChildren()
        exoPlayerView.setPlayer(null)

        mPlayer = null

        TokopediaPlayManager.getInstance(context).releasePlayer()
    }

    override fun playerPause() {
        masterJob.cancelChildren()
        TokopediaPlayManager.getInstance(context).pauseCurrentVideo()
    }

    override fun playerPlay() {
        if(ConnectionUtils.isWifiConnected(context) && isDeviceHasRequirementAutoPlay() && mPlayer?.isPlaying == false){
            masterJob.cancelChildren()
            launch(coroutineContext){
                delay(500)
                TokopediaPlayManager.getInstance(context).resumeCurrentVideo()
            }
        }
    }

    fun setUrl(url: String){
        videosUri = Uri.parse(url)
    }

    fun play(url: String){
        if(ConnectionUtils.isWifiConnected(context) && isDeviceHasRequirementAutoPlay() && !isPlayerPlaying()) {
            videosUri = Uri.parse(url)
            exoPlayerView.setPlayer(mPlayer)
            TokopediaPlayManager.getInstance(context).safePlayVideoWithUriString(url, autoPlay = false)
            muteVideoPlayer()
            playerPlay()
        }else{
            playerPause()
        }
    }

    fun preparePlayer(){
        if(ConnectionUtils.isWifiConnected(context) && isDeviceHasRequirementAutoPlay() && !isPlayerPlaying()) {
            exoPlayerView.setPlayer(mPlayer)
            TokopediaPlayManager.getInstance(context).safePlayVideoWithUri(videosUri ?: Uri.parse(""), autoPlay = false)
            muteVideoPlayer()
            playerPlay()
        }
    }

    override fun isPlayerPlaying() = TokopediaPlayManager.getInstance(context).isVideoPlaying()

    override fun seekToDefaultPosition() {
        mPlayer?.seekTo(mResumePosition)
    }

    override fun onViewAttach() {
        preparePlayer()
    }

    override fun onViewDetach() {
        launch(Dispatchers.Main) {
            exoPlayerView.setPlayer(null)
            playerPause()
        }
    }

    override fun setExoPlayerEventsListener(pExoPlayerListenerListener: ExoPlayerListener?) {
        mExoPlayerListener = pExoPlayerListenerListener
    }

    override fun onActivityResume() {
        launch(coroutineContext){
            delay(500)
            observeVideoPlayer()
            muteVideoPlayer()
            withContext(Dispatchers.Main) {
                muteVideoPlayer()
                exoPlayerView.setPlayer(mPlayer)
            }
            delay(2500)
            TokopediaPlayManager.getInstance(context).resumeCurrentVideo()
        }
    }

    override fun onActivityPause() {
        exoPlayerView.setPlayer(null)
        removeVideoPlayerObserver()
    }

    override fun onActivityStop() {
        exoPlayerView.setPlayer(null)
        mPlayer?.release()
        mPlayer = null
        removeVideoPlayerObserver()
    }

    private fun observeVideoPlayer() {
        TokopediaPlayManager.getInstance(context).getObservableVideoPlayer().observeForever(playerObserver)
        TokopediaPlayManager.getInstance(context).getObservablePlayVideoState().observeForever(playerStateObserver)
    }

    private fun removeVideoPlayerObserver() {
        TokopediaPlayManager.getInstance(context).getObservableVideoPlayer().removeObserver(playerObserver)
        TokopediaPlayManager.getInstance(context).getObservablePlayVideoState().removeObserver(playerStateObserver)
    }

    private fun muteVideoPlayer() {
        TokopediaPlayManager.getInstance(context).muteVideo(true)
        TokopediaPlayManager.getInstance(context).setRepeatMode(true)
    }

    private fun stopVideoPlayer() {
        TokopediaPlayManager.getInstance(context).stopPlayer()
    }

}