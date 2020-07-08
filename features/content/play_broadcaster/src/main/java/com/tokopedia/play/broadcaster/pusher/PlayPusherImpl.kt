package com.tokopedia.play.broadcaster.pusher

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alivc.live.pusher.*
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherStatus
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherTimerInfoState
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimerListener
import com.tokopedia.play.broadcaster.pusher.type.PlayPusherQualityMode


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImpl(private val builder: PlayPusherBuilder) : PlayPusher {

    private var mTimerDuration: PlayPusherTimer? = null
    private var mIngestUrl: String = ""

    private  var mAliVcLivePusher: AlivcLivePusher? = null
    private  var mPlayPusherStatus: PlayPusherStatus = PlayPusherStatus.Idle

    private val _observableInfoState = MutableLiveData<PlayPusherTimerInfoState>()
    private val _observableNetworkState = MutableLiveData<PlayPusherNetworkState>()
    
    private val mAliVcLivePushConfig: AlivcLivePushConfig = AlivcLivePushConfig().apply {
        setCameraType(builder.cameraType)
        setPreviewOrientation(builder.orientation)
        previewDisplayMode = builder.previewDisplayMode
        setResolution(builder.resolution)
        isEnableAutoResolution = builder.isEnableAutoResolution
        setFps(builder.fps)
        setAudioChannels(builder.audioChannel)
        audioProfile = builder.audioProfile
        setAudioEncodeMode(builder.audioEncode)
        setAudioSamepleRate(builder.audioSampleRate)
        audioBitRate = builder.audioBitrate
        setQualityMode(this)
    }

    override suspend fun create() {
        if (mAliVcLivePusher != null) {
            mAliVcLivePusher?.destroy()
            mAliVcLivePusher = null
        }
        try {
            mAliVcLivePusher = AlivcLivePusher()
            mAliVcLivePusher?.init(builder.context, mAliVcLivePushConfig)
            mAliVcLivePusher?.setLivePushErrorListener(mAliVcLivePushErrorListener)
            mAliVcLivePusher?.setLivePushNetworkListener(mAliVcLivePushNetworkListener)
        } catch (e: Exception) {
            // crashlytics
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun startPreview(surfaceView: SurfaceView) {
        if (ActivityCompat.checkSelfPermission(builder.context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            if (GlobalConfig.DEBUG)
                throw IllegalStateException("android.permission.CAMERA not granted")
            return
        }
        mAliVcLivePusher?.startPreview(surfaceView)
    }

    override fun stopPreview() {
        try {
            mAliVcLivePusher?.stopPreview()
        } catch (e: Exception) {
            // crashlytics
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun startPush(ingestUrl: String) {
        if (ingestUrl.isNotEmpty()) {
            this.mIngestUrl = ingestUrl
        }
        if (this.mIngestUrl.isEmpty()) {
            throw IllegalArgumentException("ingestUrl must not be empty")
        }
        if (isPushing() || mPlayPusherStatus != PlayPusherStatus.Idle) {
            throw IllegalStateException("Current pusher status is ${mPlayPusherStatus.name}")
        }
        try {
            mAliVcLivePusher?.startPushAysnc(this.mIngestUrl)
            mTimerDuration?.start()
            mPlayPusherStatus = PlayPusherStatus.Active
        } catch (e: Exception) {
            // crashlytics
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
                throw IllegalStateException(e)
            }
        }
    }

    override suspend fun restartPush() {
        try {
            mAliVcLivePusher?.restartPushAync()
        } catch (e: Exception) {
            // crashlytics
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
                throw IllegalStateException(e)
            }
        }
    }

    override suspend fun stopPush() {
        if (!isPushing()) return
        try {
            mAliVcLivePusher?.stopPush()
            mTimerDuration?.stop()
            mPlayPusherStatus = PlayPusherStatus.Stop
        } catch (e: Exception) {
            // crashlytics
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun switchCamera() {
        mAliVcLivePusher?.switchCamera()
    }

    override suspend fun resume() {
        if (isPushing() || mPlayPusherStatus != PlayPusherStatus.Paused) {
            throw IllegalStateException("Current pusher status is ${mPlayPusherStatus.name}")
        }
        try {
            mAliVcLivePusher?.resumeAsync()
            mTimerDuration?.resume()
            mPlayPusherStatus = PlayPusherStatus.Active
        } catch (e: Exception) {
            // crashlytics
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
                throw IllegalStateException(e)
            }
        }
    }

    override suspend fun pause() {
        if (!isPushing() || mPlayPusherStatus != PlayPusherStatus.Active) {
            throw IllegalStateException("Current pusher status is ${mPlayPusherStatus.name}")
        }
        try {
                mAliVcLivePusher?.pause()
                mTimerDuration?.pause()
                mPlayPusherStatus = PlayPusherStatus.Paused
            } catch (e: Exception) {
                // crashlytics
                if (GlobalConfig.DEBUG) {
                    e.printStackTrace()
                    throw IllegalStateException(e)
                }
            }
    }

    override suspend fun destroy() {
        try {
            mAliVcLivePusher?.destroy()
            mTimerDuration?.destroy()
        } catch (e: Exception) {
            // crashlytics
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
                throw IllegalStateException(e)
            }
        }
    }

    override fun addMaxStreamDuration(millis: Long) {
        if (this.mTimerDuration == null)
            this.mTimerDuration = PlayPusherTimer(builder.context, duration = millis)

        this.mTimerDuration?.callback = mPlayPusherTimerListener
    }

    override fun restartStreamDuration(millis: Long) {
        this.mTimerDuration?.restart(millis)
    }

    override fun addMaxPauseDuration(millis: Long) {
        if (this.mTimerDuration == null)
            this.mTimerDuration = PlayPusherTimer(builder.context)

        this.mTimerDuration?.pauseDuration = millis
        this.mTimerDuration?.callback = mPlayPusherTimerListener
    }

    override fun getObservablePlayPusherInfoState(): LiveData<PlayPusherTimerInfoState> {
        return _observableInfoState
    }

    override fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState> {
        return _observableNetworkState
    }

    private fun setQualityMode(mAliVcLivePushConfig: AlivcLivePushConfig) {
        builder.qualityMode.let {
            when(it) {
                PlayPusherQualityMode.FluencyFirst ->
                    mAliVcLivePushConfig.qualityMode = AlivcQualityModeEnum.QM_FLUENCY_FIRST
                PlayPusherQualityMode.ResolutionFirst ->
                    mAliVcLivePushConfig.qualityMode = AlivcQualityModeEnum.QM_RESOLUTION_FIRST
                is PlayPusherQualityMode.CustomBitrate -> {
                    mAliVcLivePushConfig.qualityMode = AlivcQualityModeEnum.QM_CUSTOM
                    mAliVcLivePushConfig.setTargetVideoBitrate(it.target)
                    mAliVcLivePushConfig.setMinVideoBitrate(it.min)
                    mAliVcLivePushConfig.setInitialVideoBitrate(it.init)
                }
            }
        }
    }

    private val mAliVcLivePushErrorListener = object : AlivcLivePushErrorListener {
        override fun onSystemError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
            showLog("onSystemError currentStatus:${pusher?.currentStatus}, lastError:${pusher?.lastError}, isNetworkPushing:${pusher?.isNetworkPushing}, isPushing:${pusher?.isPushing}")
        }

        override fun onSDKError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
            showLog("onSDKError currentStatus:${pusher?.currentStatus}, lastError:${pusher?.lastError}, isNetworkPushing:${pusher?.isNetworkPushing}, isPushing:${pusher?.isPushing}")
        }
    }

    private val mAliVcLivePushNetworkListener = object: AlivcLivePushNetworkListener {
        override fun onNetworkRecovery(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.Recover)
        }

        override fun onSendMessage(pusher: AlivcLivePusher?) {
        }

        override fun onReconnectFail(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.Loss)
        }

        override fun onSendDataTimeout(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.Loss)
        }

        override fun onConnectFail(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.Loss)
        }

        override fun onReconnectStart(pusher: AlivcLivePusher?) {
        }

        override fun onReconnectSucceed(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.Recover)
        }

        override fun onPushURLAuthenticationOverdue(pusher: AlivcLivePusher?): String {
            return ""
        }

        override fun onNetworkPoor(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.Poor)
        }
    }

    private val mPlayPusherTimerListener = object : PlayPusherTimerListener{
        override fun onCountDownActive(timeLeft: String) {
            _observableInfoState.postValue(PlayPusherTimerInfoState.TimerActive(timeLeft))
        }

        override fun onCountDownAlmostFinish(minutesUntilFinished: Long) {
            _observableInfoState.postValue(PlayPusherTimerInfoState.TimerAlmostFinish(minutesUntilFinished))
        }

        override fun onCountDownFinish(timeElapsed: String) {
            _observableInfoState.postValue(PlayPusherTimerInfoState.TimerFinish(timeElapsed))
        }

        override fun onReachMaximumPauseDuration() {
            _observableInfoState.postValue(PlayPusherTimerInfoState.ReachMaximumPauseDuration)
        }
    }

     override fun isPushing(): Boolean = mAliVcLivePusher?.currentStatus == AlivcLivePushStats.PUSHED

    private fun showLog(message: String) {
        if (GlobalConfig.DEBUG) {
            Log.d(TAG_PLAY_PUSHER, message)
        }
    }

    companion object {
        const val AUDIO_BITRATE_128Kbps = 128000
        const val TAG_PLAY_PUSHER = "play-broadcaster-pusher"
    }
}