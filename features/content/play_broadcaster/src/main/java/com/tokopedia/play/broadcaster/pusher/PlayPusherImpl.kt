package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.os.Build
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import com.alivc.live.pusher.*
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusherImpl(private val builder: Builder) : PlayPusher {

    // TODO("handle reconnect async")

    private var mCountDownTimer: PlayPusherCountDownTimer? = null
    private var mIngestUrl: String = ""

    private var mAliVcLivePusher: AlivcLivePusher? = null
    private var mAliVcLivePushConfig: AlivcLivePushConfig? = null

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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun create() {
        try {
            mAliVcLivePusher = AlivcLivePusher()
            mAliVcLivePusher?.init(builder.context, mAliVcLivePushConfig)
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

    override fun addCountDownTimer(countDownTimer: PlayPusherCountDownTimer) {
        this.mCountDownTimer = countDownTimer
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

    open class Builder(@ApplicationContext var context: Context) {

        var cameraType: AlivcLivePushCameraTypeEnum = AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT
            private set
        var orientation = AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT
            private set
        var previewDisplayMode = AlivcPreviewDisplayMode.ALIVC_LIVE_PUSHER_PREVIEW_ASPECT_FILL
            private set
        var resolution = AlivcResolutionEnum.RESOLUTION_720P
            private set
        var isEnableAutoResolution = true
            private set
        var fps = AlivcFpsEnum.FPS_20
            private set
        var qualityMode: PlayPusherQualityMode = PlayPusherQualityMode.FluencyFirst
            private set
        var isEnableBitrateControl = false
            private set
        var audioChannel = AlivcAudioChannelEnum.AUDIO_CHANNEL_ONE
            private set
        var audioProfile = AlivcAudioAACProfileEnum.AAC_LC
            private set
        var audioEncode = AlivcEncodeModeEnum.Encode_MODE_HARD
            private set
        var audioSampleRate = AlivcAudioSampleRateEnum.AUDIO_SAMPLE_RATE_44100
            private set
        var audioBitrate = AUDIO_BITRATE_128Kbps
            private set

        fun cameraType(cameraType: AlivcLivePushCameraTypeEnum) = apply {
            this.cameraType = cameraType
        }

        fun orientation(orientationEnum: AlivcPreviewOrientationEnum) = apply {
            this.orientation = orientationEnum
        }

        fun previewDisplayMode(previewDisplayMode: AlivcPreviewDisplayMode) = apply {
            this.previewDisplayMode = previewDisplayMode
        }

        fun resolution(resolutionEnum: AlivcResolutionEnum) = apply {
            this.resolution = resolutionEnum
        }

        fun enableAutoResolution(enable: Boolean) = apply {
            this.isEnableAutoResolution = enable
        }

        fun fps(fpsEnum: AlivcFpsEnum) = apply {
            this.fps = fpsEnum
        }

        fun enableBitrateControl(enable: Boolean) = apply {
            this.isEnableBitrateControl = enable
        }

        fun qualityMode(qualityMode: PlayPusherQualityMode) = apply {
            this.qualityMode = qualityMode
        }

        fun audioChannel(audioChannelEnum: AlivcAudioChannelEnum) = apply {
            this.audioChannel = audioChannelEnum
        }

        fun audioProfile(audioAACProfileEnum: AlivcAudioAACProfileEnum) = apply {
            this.audioProfile = audioAACProfileEnum
        }

        fun audioEncode(encodeModeEnum: AlivcEncodeModeEnum) = apply {
            this.audioEncode = encodeModeEnum
        }

        fun audioSampleRate(sampleRateEnum: AlivcAudioSampleRateEnum) = apply {
            this.audioSampleRate = sampleRateEnum
        }

        fun audioBitrate(bitrate: Int) = apply {
            this.audioBitrate = bitrate
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        fun build() = PlayPusherImpl(this)
    }

    companion object {

        const val AUDIO_BITRATE_128Kbps = 128000
    }
}