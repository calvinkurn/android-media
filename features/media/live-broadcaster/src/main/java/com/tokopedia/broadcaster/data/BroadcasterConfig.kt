package com.tokopedia.broadcaster.data

import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer

enum class AudioType(val status: String) {
    MIC("MIC"),
    PCM("PCM"),
}

enum class BitrateMode(val status: String) {
    LadderAscend("LadderAscend"),
    LogarithmicDescend("LogarithmicDescend"),
}

data class BroadcasterConfig(
    var ingestUrl: String = "",
    var videoWidth: Int = 1280,
    var videoHeight: Int = 720,
    var fps: Float = 30.0f,
    var videoBitrate: Int = 2000 * 1000, // Kbps -> bps
    var audioRate: Int = 44100, // 44.1kHz
    var audioChannelCount: Int = 1,
    var audioSource: Int = 5,
    var audioType: AudioType = AudioType.MIC, // we have MIC and PCM
    var maxRetry: Int = 3,
    var reconnectDelay: Int = 3000,
    var bitrateMode: BitrateMode = BitrateMode.LadderAscend
) {

    val audioBitrate: Int = AudioConfig.calcBitRate(
        audioRate,
        audioChannelCount,
        AudioConfig.AAC_PROFILE
    )

    fun ingestUrl(value: String) = apply {
        ingestUrl = value
        return this
    }

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