package com.tokopedia.home.beranda.presentation.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
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
                is TokopediaPlayVideoState.Buffering -> mExoPlayerListener?.onPlayerBuffering()
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
        TokopediaPlayManager.getInstance(context).stopPlayer()
    }

    override fun playerPlay() {
        if(DeviceConnectionInfo.isConnectWifi(context) && isDeviceHasRequirementAutoPlay() && mPlayer?.isPlaying == false){
            masterJob.cancelChildren()
            launch(coroutineContext){
                delay(1000)
                TokopediaPlayManager.getInstance(context).resumeCurrentVideo()
            }
        }
    }

    fun play(url: String){
        if(DeviceConnectionInfo.isConnectWifi(context) && isDeviceHasRequirementAutoPlay() && !isPlayerPlaying()) {
            videosUri = Uri.parse(url)
            exoPlayerView.setPlayer(mPlayer)
            muteVideoPlayer()
            TokopediaPlayManager.getInstance(context).safePlayVideoWithUriString(url, autoPlay = false)
            playerPlay()
        }else{
            playerPause()
        }
    }

    fun preparePlayer(){
        if(DeviceConnectionInfo.isConnectWifi(context) && isDeviceHasRequirementAutoPlay() && !isPlayerPlaying()) {
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
        if(DeviceConnectionInfo.isConnectWifi(context) && isDeviceHasRequirementAutoPlay()) {
            masterJob.cancelChildren()
            launch(coroutineContext) {
                delay(3000)
                observeVideoPlayer()
                withContext(Dispatchers.Main) {
                    exoPlayerView.setPlayer(mPlayer)
                    muteVideoPlayer()
                    TokopediaPlayManager.getInstance(context).safePlayVideoWithUri(videosUri ?: Uri.parse(""), autoPlay = true)
                }
            }
        } else {
            stopVideoPlayer()
        }
    }

    override fun onActivityPause() {
        masterJob.cancelChildren()
        exoPlayerView.setPlayer(null)
        removeVideoPlayerObserver()
    }

    override fun onActivityStop() {
        masterJob.cancelChildren()
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