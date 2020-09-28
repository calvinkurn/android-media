package com.tokopedia.play.broadcaster.pusher

import android.Manifest
import android.content.pm.PackageManager
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alivc.live.pusher.*
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoListener
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherStatus
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimerListener
import com.tokopedia.play.broadcaster.pusher.type.PlayPusherQualityMode
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play.broadcaster.util.extension.sendCrashlyticsLog


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImpl(private val builder: PlayPusherBuilder) : PlayPusher {

    private var mTimerDuration: PlayPusherTimer? = null
    private var mIngestUrl: String = ""

    private  var mAliVcLivePusher: AlivcLivePusher? = null
    private  var mPlayPusherStatus: PlayPusherStatus = PlayPusherStatus.Idle

    private var  mPusherListener: PlayPusherInfoListener? = null
    private val _observableNetworkState = MutableLiveData<PlayPusherNetworkState>()
    
    private val mAliVcLivePushConfig: PlayPusherConfig = PlayPusherConfig().apply {
        setCameraType(builder.cameraType)
        setPreviewOrientation(builder.orientation)
        previewDisplayMode = builder.previewDisplayMode
        setResolution(builder.resolution)
        isEnableAutoResolution = builder.isEnableAutoResolution
        setFps(builder.fps)
        setPushMirror(builder.pushMirror)
//        setAudioChannels(builder.audioChannel)
//        audioProfile = builder.audioProfile
//        setAudioEncodeMode(builder.audioEncode)
//        setAudioSamepleRate(builder.audioSampleRate)
//        audioBitRate = builder.audioBitrate
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
            mAliVcLivePusher?.setAudioDenoise(true)
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
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
        try {
            mAliVcLivePusher?.startPreview(surfaceView)
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
    }

    override fun stopPreview() {
        try {
            mAliVcLivePusher?.stopPreview()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
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
        mTimerDuration?.start()
        mAliVcLivePusher?.startPushAysnc(this.mIngestUrl)
        mPlayPusherStatus = PlayPusherStatus.Active
        mPusherListener?.onStart()
    }

    override suspend fun restartPush() {
        mAliVcLivePusher?.restartPushAync()
    }

    override suspend fun stopPush() {
        try {
            mAliVcLivePusher?.stopPush()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
        mTimerDuration?.stop()
        mPlayPusherStatus = PlayPusherStatus.Stop
        mPusherListener?.onStop(mTimerDuration?.getTimeElapsed().orEmpty())
    }

    override suspend fun switchCamera() {
        try {
            mAliVcLivePusher?.switchCamera()
            mAliVcLivePusher?.setPushMirror(mAliVcLivePushConfig.getCameraTypeEnum()
                    == AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT)
        } catch (e: Exception) {}
    }

    override suspend fun resume() {
        try {
            mAliVcLivePusher?.resumeAsync()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
        if (mPlayPusherStatus != PlayPusherStatus.Paused) {
            throw IllegalStateException("Current pusher status is ${mPlayPusherStatus.name}")
        } else {
            mTimerDuration?.resume()
            mPlayPusherStatus = PlayPusherStatus.Active
            mPusherListener?.onResume()
        }
    }

    override suspend fun pause() {
        try {
            mAliVcLivePusher?.pause()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
        if (mPlayPusherStatus != PlayPusherStatus.Active) {
            throw IllegalStateException("Current pusher status is ${mPlayPusherStatus.name}")
        } else {
            mTimerDuration?.pause()
            mPlayPusherStatus = PlayPusherStatus.Paused
            mPusherListener?.onPause()
        }
    }

    override fun destroy() {
        try {
            mAliVcLivePusher?.destroy()
            mTimerDuration?.destroy()
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun addStreamDuration(durationInMillis: Long) {
        if (this.mTimerDuration == null)
            this.mTimerDuration = PlayPusherTimer(builder.context, durationInMillis)

        this.mTimerDuration?.callback = mPlayPusherTimerListener
    }

    override fun addMaxStreamDuration(durationInMillis: Long) {
        this.mTimerDuration?.mMaxDuration = durationInMillis
    }

    override fun restartStreamDuration(durationInMillis: Long) {
        this.mTimerDuration?.restart(durationInMillis)
    }

    override fun addMaxPauseDuration(durationInMillis: Long) {
        if (this.mTimerDuration == null)
            this.mTimerDuration = PlayPusherTimer(builder.context)

        this.mTimerDuration?.pauseDuration = durationInMillis
        this.mTimerDuration?.callback = mPlayPusherTimerListener
    }

    override fun addPusherInfoListener(playPusherInfoListener: PlayPusherInfoListener) {
        this.mPusherListener = playPusherInfoListener
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
            sendCrashlyticsLog(DefaultErrorThrowable("onSystemError ${pusherError?.msg}, lastError:${pusher?.lastError}"))
        }

        override fun onSDKError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
            sendCrashlyticsLog(DefaultErrorThrowable("onSystemError ${pusherError?.msg}, lastError:${pusher?.lastError}"))
        }
    }

    private val mAliVcLivePushNetworkListener = object: AlivcLivePushNetworkListener {
        override fun onNetworkRecovery(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.Recover)
        }

        override fun onSendMessage(pusher: AlivcLivePusher?) {
        }

        override fun onReconnectFail(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.ReconnectFailed)
        }

        override fun onSendDataTimeout(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.Loss)
        }

        override fun onConnectFail(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.ConnectFailed)
        }

        override fun onReconnectStart(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.ReConnectStart)
        }

        override fun onReconnectSucceed(pusher: AlivcLivePusher?) {
            _observableNetworkState.postValue(PlayPusherNetworkState.ReConnectSucceed)
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
            mPusherListener?.onTimerActive(timeLeft)
        }

        override fun onCountDownAlmostFinish(minutesUntilFinished: Long) {
            mPusherListener?.onTimerAlmostFinish(minutesUntilFinished)
        }

        override fun onCountDownFinish() {
            mPusherListener?.onTimerFinish()
        }

        override fun onReachMaximumPauseDuration() {
            mPusherListener?.onReachMaximumPauseDuration()
        }
    }

    override fun isPushing(): Boolean = mAliVcLivePusher?.currentStatus == AlivcLivePushStats.PUSHED

    companion object {
        const val AUDIO_BITRATE_128Kbps = 128000
    }
}