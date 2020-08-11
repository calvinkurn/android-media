package com.tokopedia.home.beranda.presentation.view.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.util.DimensionUtils
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.state.PlayVideoState
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
        private const val DELAY_BACK = 1000L
    }

    var isAutoPlay = true

    private var mPlayer: ExoPlayer? = null
    private var mExoPlayerListener: ExoPlayerListener? = null

    private var videoUri: Uri? = null

    private var playConnectionMonitor = PlayConnectionMonitor(context)

    private val playManager
        get() = PlayVideoManager.getInstance(context)

    private val timerDelayBack = object : CountDownTimer(DELAY_BACK, 1000) {
        override fun onFinish() {
            observeVideoPlayer()
            resumeVideo()
        }

        override fun onTick(millisUntilFinished: Long) {}
    }

    private val timerDelayPlay = object : CountDownTimer(DELAY_PLAYING, 1000) {
        override fun onFinish() {
            playManager.resume()
        }

        override fun onTick(millisUntilFinished: Long) {}
    }

    /**
     * DO NOT CHANGE THIS TO LAMBDA
     */
    private val playerObserver = object : Observer<ExoPlayer> {
        override fun onChanged(t: ExoPlayer?) {
            mPlayer = t
        }
    }

    private val playerStateObserver = object : Observer<PlayVideoState>{
        override fun onChanged(state: PlayVideoState) {
            when(state){
                is PlayVideoState.NoMedia -> mExoPlayerListener?.onPlayerIdle()
                is PlayVideoState.Error -> mExoPlayerListener?.onPlayerError(state.error.message)
                is PlayVideoState.Pause -> mExoPlayerListener?.onPlayerPaused()
                is PlayVideoState.Buffering -> mExoPlayerListener?.onPlayerBuffering()
                is PlayVideoState.Playing -> mExoPlayerListener?.onPlayerPlaying()
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
        playManager.release()
    }

    override fun playerPause() {
        masterJob.cancelChildren()
        exoPlayerView.setPlayer(null)
        playManager.stop()
    }

    override fun playerPlayWithDelay() {
        masterJob.cancelChildren()
        timerDelayPlay.start()
    }

    override fun play(url: String){
        if(DeviceConnectionInfo.isConnectWifi(context) &&
                isDeviceHasRequirementAutoPlay() &&
                (!isPlayerPlaying() || url != videoUri.toString())) {
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
        if(videoUri != null && !videoUri?.toString().isNullOrEmpty()
                && isDeviceHasRequirementAutoPlay()
                && isAutoPlay) {
            playManager.playUri(videoUri ?: Uri.parse(""), autoPlay = false)
            muteVideoPlayer()
            exoPlayerView.setPlayer(mPlayer)
            playerPlayWithDelay()
        }
    }

    override fun isPlayerPlaying() = PlayVideoManager.getInstance(context).isPlaying()

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
            timerDelayBack.start()
        } else {
            stopVideoPlayer()
        }
    }

    override fun onActivityPause() {
        playerPause()
        removeVideoPlayerObserver()
    }

    override fun onActivityDestroy() {
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
        playManager.mute(true)
        playManager.setRepeatMode(true)
    }

    private fun stopVideoPlayer() {
        playManager.stop()
    }

}