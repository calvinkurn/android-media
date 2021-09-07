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
    var audioBitrate: Int = AudioConfig.calcBitRate(
        audioRate,
        audioChannelCount,
        AudioConfig.AAC_PROFILE
    ),
    var audioType: AudioType = AudioType.MIC, // we have MIC and PCM
    var maxRetry: Int = 3,
    var reconnectDelay: Int = 3000,
    var bitrateMode: BitrateMode = BitrateMode.LadderAscend
) {
    fun getVideoSize() = Streamer.Size(videoHeight, videoWidth)

    fun getAudioType(): AudioConfig.INPUT_TYPE {
        return when (audioType) {
            AudioType.MIC -> AudioConfig.INPUT_TYPE.MIC
            AudioType.PCM -> AudioConfig.INPUT_TYPE.PCM
        }
    }
}