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
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherErrorType
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimerListener
import com.tokopedia.play.broadcaster.pusher.type.PlayPusherQualityMode


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImpl(private val builder: PlayPusherBuilder) : PlayPusher {

    private var mTimerDuration: PlayPusherTimer? = null
    private var mIngestUrl: String = ""

    private var mAliVcLivePusher: AlivcLivePusher = AlivcLivePusher()

    private val _observableInfoState = MutableLiveData<PlayPusherInfoState>()
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

    override fun create() {
        try {
            mAliVcLivePusher = AlivcLivePusher()
            mAliVcLivePusher.init(builder.context, mAliVcLivePushConfig)
            mAliVcLivePusher.setLivePushErrorListener(mAliVcLivePushErrorListener)
//            mAliVcLivePusher.setLivePushInfoListener(mAliVcLivePushInfoListener)
            mAliVcLivePusher.setLivePushNetworkListener(mAliVcLivePushNetworkListener)
        } catch (e: IllegalArgumentException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun startPreview(surfaceView: SurfaceView) {
        try {
            if (ActivityCompat.checkSelfPermission(builder.context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                mAliVcLivePusher.startPreviewAysnc(surfaceView)
            }
        } catch (e: IllegalArgumentException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun stopPreview() {
        try {
            mAliVcLivePusher.stopPreview()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun startPush(ingestUrl: String) {
        if (ingestUrl.isNotEmpty()) {
            this.mIngestUrl = ingestUrl
        }
        if (this.mIngestUrl.isEmpty()) {
            if (GlobalConfig.DEBUG) {
                throw IllegalArgumentException("ingestUrl is empty")
            }
            return
        }
        try {
            mAliVcLivePusher.startPushAysnc(this.mIngestUrl)
            mTimerDuration?.start()
        } catch (e: Exception) {
            // TODO("handle start push async error")
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun restartPush() {
        try {
            mAliVcLivePusher.restartPushAync()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun stopPush() {
        try {
            mAliVcLivePusher.stopPush()
            mTimerDuration?.stop()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun switchCamera() {
        try {
            mAliVcLivePusher.switchCamera()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun resume() {
        try {
            mAliVcLivePusher.resumeAsync()
            mTimerDuration?.resume()
        } catch (e: java.lang.IllegalStateException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        } catch (e: java.lang.IllegalArgumentException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun pause() {
        try {
            mAliVcLivePusher.pause()
            mTimerDuration?.pause()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun destroy() {
        try {
            mAliVcLivePusher.destroy()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun addMaxStreamDuration(millis: Long) {
        this.mTimerDuration = PlayPusherTimer(
                context = builder.context,
                duration = millis,
                callback = mPlayPusherTimerListener
        )
    }

    override fun addMaxPauseDuration(millis: Long) {
        this.mTimerDuration?.pauseDuration = millis
    }

    override fun getObservablePlayPusherInfoState(): LiveData<PlayPusherInfoState> {
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

    private val mAliVcLivePushInfoListener = object: AlivcLivePushInfoListener {
        override fun onPushResumed(pusher: AlivcLivePusher?) {
        }

        override fun onPreviewStarted(pusher: AlivcLivePusher?) {
        }

        override fun onAdjustFps(pusher: AlivcLivePusher?, curFps: Int, targetFps: Int) {
        }

        override fun onFirstFramePreviewed(pusher: AlivcLivePusher?) {
        }

        override fun onPushStoped(pusher: AlivcLivePusher?) {
        }

        override fun onDropFrame(pusher: AlivcLivePusher?, countBef: Int, countAft: Int) {
        }

        override fun onFirstAVFramePushed(pusher: AlivcLivePusher?) {
        }

        override fun onPreviewStoped(pusher: AlivcLivePusher?) {
        }

        override fun onAdjustBitRate(pusher: AlivcLivePusher?, curBr: Int, targetBr: Int) {
        }

        override fun onPushStarted(pusher: AlivcLivePusher?) {
        }

        override fun onPushPauesed(pusher: AlivcLivePusher?) {
        }

        override fun onPushRestarted(pusher: AlivcLivePusher?) {
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
            _observableInfoState.postValue(PlayPusherInfoState.Active(timeLeft))
        }

        override fun onCountDownAlmostFinish(minutesUntilFinished: Long) {
            _observableInfoState.postValue(PlayPusherInfoState.AlmostFinish(minutesUntilFinished))
        }

        override fun onCountDownFinish(timeElapsed: String) {
            _observableInfoState.postValue(PlayPusherInfoState.Finish(timeElapsed))
        }

        override fun onReachMaximumPauseDuration() {
            _observableInfoState.postValue(PlayPusherInfoState.Error(PlayPusherErrorType.ReachMaximumDuration))
        }
    }

     override fun isPushing(): Boolean = mAliVcLivePusher.currentStatus == AlivcLivePushStats.PUSHED

    private fun showLog(message: String) {
        if (GlobalConfig.DEBUG) {
            Log.d(TAG_PLAY_PUSHER, message)
        }
    }

    companion object {
        const val AUDIO_BITRATE_128Kbps = 128000
        const val TAG_PLAY_PUSHER = "PlayPusher"
    }
}