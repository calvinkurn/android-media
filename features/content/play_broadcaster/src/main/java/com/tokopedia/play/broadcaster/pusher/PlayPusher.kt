package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.view.SurfaceView
import androidx.lifecycle.LiveData
import com.alivc.live.pusher.*
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.pusher.type.PlayPusherQualityMode
import com.tokopedia.play.broadcaster.util.DeviceInfoUtil


/**
 * Created by mzennis on 24/05/20.
 */
interface PlayPusher {

    /**
     * Initialize SDK
     */
    fun create()

    /**
     * Open camera for preview live streaming
     */
    fun startPreview(surfaceView: SurfaceView)

    /**
     * Stop previewing
     */
    fun stopPreview()

    /**
     * Start live streaming by providing ingest url
     */
    fun startPush(ingestUrl: String)

    /**
     * Restart live streaming
     */
    fun restartPush()

    /**
     * Stop live streaming
     */
    fun stopPush()

    /**
     * Switch camera between front and back, the default is front
     */
    fun switchCamera()

    /**
     * Call this method on Activity / Fragment LifeCycle: onResume()
     */
    fun resume()

    /**
     * Call this method on Activity / Fragment LifeCycle: onPause()
     */
    fun pause()

    /**
     * Call this method on Activity / Fragment LifeCycle: onDestroy()
     */
    fun destroy()

    /**
     * add maximum live streaming duration, the default is 30minutes
     */
    fun addMaxStreamDuration(millis: Long)

    /**
     * Get Active & Finish Live Streaming State
     */
    fun getObservablePlayPusherInfoState(): LiveData<PlayPusherInfoState>

    /**
     * Get Network State when Live Streaming
     */
    fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState>

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
        var audioBitrate = PlayPusherImpl.AUDIO_BITRATE_128Kbps

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

        fun build() = if (DeviceInfoUtil.isDeviceSupported()) {
            PlayPusherImpl(this)
        } else {
            PlayPusherImplNoop(this)
        }
    }
}