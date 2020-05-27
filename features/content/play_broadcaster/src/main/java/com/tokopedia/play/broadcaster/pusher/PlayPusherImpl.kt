package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alivc.live.pusher.AlivcLivePushConfig
import com.alivc.live.pusher.AlivcLivePushNetworkListener
import com.alivc.live.pusher.AlivcLivePusher
import com.alivc.live.pusher.AlivcQualityModeEnum
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherCountDownTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherCountDownTimerListener
import com.tokopedia.play.broadcaster.pusher.type.PlayPusherQualityMode


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImpl(private val builder: PlayPusher.Builder) : PlayPusher {

    private var mCountDownTimer: PlayPusherCountDownTimer? = null
    private var mIngestUrl: String = ""

    private var mAliVcLivePusher: AlivcLivePusher? = null
    private var mAliVcLivePushConfig: AlivcLivePushConfig? = null

    private val _observableInfoState = MutableLiveData<PlayPusherInfoState>()
    private val _observableNetworkState = MutableLiveData<PlayPusherNetworkState>()

    init {
        mAliVcLivePushConfig = AlivcLivePushConfig()
        mAliVcLivePushConfig?.setCameraType(builder.cameraType)
        mAliVcLivePushConfig?.setPreviewOrientation(builder.orientation)
        mAliVcLivePushConfig?.previewDisplayMode = builder.previewDisplayMode
        mAliVcLivePushConfig?.setResolution(builder.resolution)
        mAliVcLivePushConfig?.isEnableAutoResolution = builder.isEnableAutoResolution
        mAliVcLivePushConfig?.setFps(builder.fps)
        setQualityMode()
        mAliVcLivePushConfig?.isEnableBitrateControl = builder.isEnableBitrateControl
        mAliVcLivePushConfig?.setAudioChannels(builder.audioChannel)
        mAliVcLivePushConfig?.audioProfile = builder.audioProfile
        mAliVcLivePushConfig?.setAudioEncodeMode(builder.audioEncode)
        mAliVcLivePushConfig?.setAudioSamepleRate(builder.audioSampleRate)
        mAliVcLivePushConfig?.audioBitRate = builder.audioBitrate
    }

    override fun create() {
        try {
            mAliVcLivePusher = AlivcLivePusher()
            mAliVcLivePusher?.init(builder.context, mAliVcLivePushConfig)
            mAliVcLivePusher?.setLivePushNetworkListener(mAliVcLivePushNetworkListener)
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
        if (mAliVcLivePusher != null) {
            try {
                mAliVcLivePusher?.startPreviewAysnc(surfaceView)
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
    }

    override fun stopPreview() {
        try {
            mAliVcLivePusher?.stopPreview()
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
            mAliVcLivePusher?.startPushAysnc(this.mIngestUrl)
            mCountDownTimer?.start()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun restartPush() {
        try {
            mAliVcLivePusher?.restartPushAync()
            mCountDownTimer?.start()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun stopPush() {
        try {
            mAliVcLivePusher?.stopPush()
            mCountDownTimer?.stop()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun switchCamera() {
        try {
            mAliVcLivePusher?.switchCamera()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun resume() {
        try {
            if (mAliVcLivePusher?.isPushing == true) {
                mAliVcLivePusher?.resumeAsync()
                mCountDownTimer?.start()
            }
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
            if (mAliVcLivePusher?.isPushing == true) {
                mCountDownTimer?.stop()
                mAliVcLivePusher?.pause()
            }
        } catch (e: java.lang.IllegalStateException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun destroy() {
        try {
            if (mAliVcLivePusher?.isPushing == true) {
                mCountDownTimer?.stop()
            }
            mAliVcLivePusher?.destroy()
        } catch (e: java.lang.IllegalStateException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }

        mAliVcLivePusher = null
        mAliVcLivePushConfig = null
    }

    override fun addMaxStreamDuration(millis: Long) {
        this.mCountDownTimer = PlayPusherCountDownTimer(builder.context, millis)
        this.mCountDownTimer?.addCallback(mCountDownTimerListener)
    }

    override fun getObservablePlayPusherInfoState(): LiveData<PlayPusherInfoState> {
        return _observableInfoState
    }

    override fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState>? {
        return _observableNetworkState
    }

    private fun setQualityMode() {
        builder.qualityMode.let {
            when(it) {
                PlayPusherQualityMode.FluencyFirst ->
                    mAliVcLivePushConfig?.qualityMode = AlivcQualityModeEnum.QM_FLUENCY_FIRST
                PlayPusherQualityMode.ResolutionFirst ->
                    mAliVcLivePushConfig?.qualityMode = AlivcQualityModeEnum.QM_RESOLUTION_FIRST
                is PlayPusherQualityMode.CustomBitrate -> {
                    mAliVcLivePushConfig?.qualityMode = AlivcQualityModeEnum.QM_CUSTOM
                    mAliVcLivePushConfig?.setTargetVideoBitrate(it.target)
                    mAliVcLivePushConfig?.setMinVideoBitrate(it.min)
                    mAliVcLivePushConfig?.setInitialVideoBitrate(it.init)
                }
            }
        }
    }

    private val mCountDownTimerListener = object: PlayPusherCountDownTimerListener {

        override fun onCountDownActive(millisUntilFinished: Long) {
            _observableInfoState.value  = PlayPusherInfoState.Active(millisUntilFinished)
        }

        override fun onCountDownFinish() {
            stopPush()
            _observableInfoState.value = PlayPusherInfoState.Finish
        }

    }

    private val mAliVcLivePushNetworkListener = object: AlivcLivePushNetworkListener {
        override fun onNetworkRecovery(pusher: AlivcLivePusher?) {
            _observableNetworkState.value = PlayPusherNetworkState.Recover
        }

        override fun onSendMessage(pusher: AlivcLivePusher?) {
        }

        override fun onReconnectFail(pusher: AlivcLivePusher?) {
        }

        override fun onSendDataTimeout(pusher: AlivcLivePusher?) {
        }

        override fun onConnectFail(pusher: AlivcLivePusher?) {
            _observableNetworkState.value = PlayPusherNetworkState.Loss
        }

        override fun onReconnectStart(pusher: AlivcLivePusher?) {
        }

        override fun onReconnectSucceed(pusher: AlivcLivePusher?) {
            _observableNetworkState.value = PlayPusherNetworkState.Recover
        }

        override fun onPushURLAuthenticationOverdue(pusher: AlivcLivePusher?): String {
            return ""
        }

        override fun onNetworkPoor(pusher: AlivcLivePusher?) {
            _observableNetworkState.value = PlayPusherNetworkState.Poor
        }

    }

    companion object {
        const val AUDIO_BITRATE_128Kbps = 128000
    }
}