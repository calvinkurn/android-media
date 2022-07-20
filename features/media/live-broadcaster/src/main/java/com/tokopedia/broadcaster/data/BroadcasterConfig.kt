package com.tokopedia.broadcaster.data

import com.tokopedia.broadcaster.mediator.LivePusherConfig
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer

data class BroadcasterConfig(
    override var videoWidth: Int = 1280,
    override var videoHeight: Int = 720,
    override var fps: Float = 30.0f,
    override var videoBitrate: Int = 2000 * 1000, // Kbps -> bps
    override var audioRate: Int = 44100, // 44.1kHz
    override var audioChannelCount: Int = 1,
    override var audioSource: Int = 5,
    override var audioType: AudioType = AudioType.MIC, // we have MIC and PCM
    override var maxRetry: Int = 3,
    override var reconnectDelay: Int = 3000,
    override var bitrateMode: BitrateMode = BitrateMode.LadderAscend,
    override var netTrackerInterval: Int = 5000
) : LivePusherConfig {

    override val audioBitrate: Int = AudioConfig.calcBitRate(
        audioRate,
        audioChannelCount,
        AudioConfig.AAC_PROFILE
    )

    fun videoSize(width: Int, height: Int) = apply {
        videoWidth = width
        videoHeight = height
        return this
    }

    fun fps(value: Float) = apply {
        fps = value
        return this
    }

    fun videoBitrate(value: Int) = apply {
        videoBitrate = value * 1000
        return this
    }

    fun audioRate(value: Int) = apply {
        audioRate = value
        return this
    }

    fun audioChannelCount(value: Int) = apply {
        audioChannelCount = value
        return this
    }

    fun audioSource(value: Int) = apply {
        audioSource = value
        return this
    }

    fun micAudio() = apply {
        audioType = AudioType.MIC
        return this
    }

    fun pcmAudio() = apply {
        audioType = AudioType.PCM
        return this
    }

    fun maxRetry(value: Int) = apply {
        maxRetry = value
        return this
    }

    fun reconnectDelay(value: Int) = apply {
        reconnectDelay = value
        return this
    }

    fun ladderAscend() = apply {
        bitrateMode = BitrateMode.LadderAscend
    }

    fun logarithmicDescend() = apply {
        bitrateMode = BitrateMode.LogarithmicDescend
    }

    fun getVideoSize() = Streamer.Size(videoHeight, videoWidth)

    fun getAudioType(): AudioConfig.INPUT_TYPE {
        return when (audioType) {
            AudioType.MIC -> AudioConfig.INPUT_TYPE.MIC
            AudioType.PCM -> AudioConfig.INPUT_TYPE.PCM
        }
    }
}