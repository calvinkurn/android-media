package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import com.alivc.live.pusher.*
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.pusher.type.PlayPusherQualityMode
import com.tokopedia.play.broadcaster.util.deviceinfo.DeviceInfoUtil


/**
 * Created by mzennis on 02/06/20.
 */
class PlayPusherBuilder(@ApplicationContext var context: Context) {

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
    var fps = AlivcFpsEnum.FPS_30
        private set
    var qualityMode: PlayPusherQualityMode = PlayPusherQualityMode.FluencyFirst
        private set
    var audioChannel = AlivcAudioChannelEnum.AUDIO_CHANNEL_TWO
        private set
    var audioProfile = AlivcAudioAACProfileEnum.AAC_LC
        private set
    var audioEncode = AlivcEncodeModeEnum.Encode_MODE_SOFT
        private set
    var audioSampleRate = AlivcAudioSampleRateEnum.AUDIO_SAMPLE_RATE_32000
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