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
import com.tokopedia.play_common.util.PlayConnectionMonitor
import com.tokopedia.play_common.util.PlayConnectionState
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

    companion object{
        private const val DELAY_PLAYING = 2000L
    }

    private var mPlayer: ExoPlayer? = null
    private var mExoPlayerListener: ExoPlayerListener? = null

    private var videoUri: Uri? = null

    private var playConnectionMonitor = PlayConnectionMonitor(context)

    private val playManager
        get() = TokopediaPlayManager.getInstance(context)

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

    private val connectionStateObserver = object :  Observer<PlayConnectionState> {
        override fun onChanged(state: PlayConnectionState?) {
            when (state) {
                is PlayConnectionState.UnAvailable -> {
                    playerPause()
                    mExoPlayerListener?.onPlayerIdle()
                }
                is PlayConnectionState.Available -> {
                    resumeVideo()
                }
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

    override fun init() {
        observeVideoPlayer()
        muteVideoPlayer()
    }

    override fun releasePlayer() {
        playManager.releasePlayer()
    }

    override fun playerPause() {
        masterJob.cancelChildren()
        exoPlayerView.setPlayer(null)
        playManager.stopPlayer()
    }

    override fun playerPlayWithDelay() {
        masterJob.cancelChildren()
        launch(coroutineContext){
            delay(DELAY_PLAYING)
            playManager.resumeCurrentVideo()
        }
    }

    override fun play(url: String){
        if(DeviceConnectionInfo.isConnectWifi(context) && isDeviceHasRequirementAutoPlay() && !isPlayerPlaying()) {
            videoUri = Uri.parse(url)
            resumeVideo()
        }else{
            playerPause()
        }
    }

    override fun preparePlayer(){
        if (DeviceConnectionInfo.isConnectWifi(context)) resumeVideo()
    }

    fun resumeVideo() {
        if(videoUri != null && videoUri.toString().isNotEmpty() && isDeviceHasRequirementAutoPlay() && !isPlayerPlaying()) {
            playManager.safePlayVideoWithUri(videoUri ?: Uri.parse(""), autoPlay = false)
            muteVideoPlayer()
            exoPlayerView.setPlayer(mPlayer)
            playerPlayWithDelay()
        }
    }

    override fun isPlayerPlaying() = TokopediaPlayManager.getInstance(context).isVideoPlaying()

    override fun onViewAttach() {
        preparePlayer()
    }

    override fun onViewDetach() {
        playerPause()
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
                    playManager.safePlayVideoWithUri(videoUri ?: Uri.parse(""), autoPlay = true)
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
        releasePlayer()
        mPlayer = null
        removeVideoPlayerObserver()
    }

    private fun observeVideoPlayer() {
        playManager.getObservableVideoPlayer().observeForever(playerObserver)
        playManager.getObservablePlayVideoState().observeForever(playerStateObserver)
        playConnectionMonitor.getObservablePlayConnectionState().observeForever(connectionStateObserver)
    }

    private fun removeVideoPlayerObserver() {
        playManager.getObservableVideoPlayer().removeObserver(playerObserver)
        playManager.getObservablePlayVideoState().removeObserver(playerStateObserver)
        playConnectionMonitor.getObservablePlayConnectionState().removeObserver(connectionStateObserver)
    }

    private fun muteVideoPlayer() {
        playManager.muteVideo(true)
        playManager.setRepeatMode(true)
    }

    private fun stopVideoPlayer() {
        playManager.stopPlayer()
    }

}