package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import com.alivc.live.pusher.*
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig


/**
 * Created by mzennis on 24/05/20.
 */
class PlayPusher(private val builder: Builder) {

    // TODO("handle reconnect async")


    var playPusherCountDownTimer: PlayPusherCountDownTimer? = null
//    var surfaceView: SurfaceView? = null
//        set(value) {
//            value?.holder?.addCallback(object : SurfaceHolder.Callback {
//                override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
//                    mSurfaceStatus = SurfaceStatus.CHANGED
//                }
//
//                override fun surfaceDestroyed(surfaceHolder: SurfaceHolder?) {
//                    mSurfaceStatus = SurfaceStatus.DESTROYED
//                }
//
//                override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
//                    if (mSurfaceStatus == SurfaceStatus.UNINITED) {
//                        mSurfaceStatus = SurfaceStatus.CREATED
//                    } else if (mSurfaceStatus == SurfaceStatus.DESTROYED) {
//                        mSurfaceStatus = SurfaceStatus.RECREATED
//                    }
//                }
//            })
//            field = value
//        }

    private var ingestUrl: String = ""

//    private var mSurfaceStatus = SurfaceStatus.UNINITED

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun create() {
        try {
            mAliVcLivePusher = AlivcLivePusher()
            mAliVcLivePusher?.init(builder.context, mAliVcLivePushConfig)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun startPreview(surfaceView: SurfaceView) {
//        if (mSurfaceStatus != SurfaceStatus.UNINITED &&
//                mSurfaceStatus != SurfaceStatus.DESTROYED &&
        if (mAliVcLivePusher != null) {
            try {
                mAliVcLivePusher?.startPreviewAysnc(surfaceView)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    fun stopPreview() {
        try {
            mAliVcLivePusher?.stopPreview()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    fun startPush(ingestUrl: String = "") {
        if (ingestUrl.isNotEmpty()) {
            this.ingestUrl = ingestUrl
        }
        if (this.ingestUrl.isEmpty()) {
            if (GlobalConfig.DEBUG) {
                throw IllegalArgumentException("ingestUrl is empty")
            }
            return
        }
        try {
            mAliVcLivePusher?.startPushAysnc(this.ingestUrl)
            playPusherCountDownTimer?.start()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    fun stopPush() {
        try {
            mAliVcLivePusher?.stopPush()
            playPusherCountDownTimer?.stop()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    fun switchCamera() {
        try {
            mAliVcLivePusher?.switchCamera()
        } catch (e: Exception) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    fun resume() {
        try {
            if (mAliVcLivePusher?.isPushing == true) {
                mAliVcLivePusher?.resumeAsync()
                playPusherCountDownTimer?.start()
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

    fun pause() {
        try {
            if (mAliVcLivePusher?.isPushing == true) {
                playPusherCountDownTimer?.stop()
                mAliVcLivePusher?.pause()
            }
        } catch (e: java.lang.IllegalStateException) {
            if (GlobalConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    fun destroy() {
        try {
            if (mAliVcLivePusher?.isPushing == true) {
                playPusherCountDownTimer?.stop()
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

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun build() = PlayPusher(this)
    }

    companion object {

        const val AUDIO_BITRATE_128Kbps = 128000
    }
}