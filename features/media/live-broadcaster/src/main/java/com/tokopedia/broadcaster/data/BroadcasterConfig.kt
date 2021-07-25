package com.tokopedia.broadcaster.data

import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer

sealed class AudioType {
    object MIC: AudioType()
    object PCM: AudioType()
}

data class BroadcasterConfig(
    var videoWidth: Int = 1280,
    var videoHeight: Int = 720,
    var fps: Float = 30.0f,
    var videoBitrate: Int = 2000 * 1000, // Kbps -> bps
    var audioRate: Int = 44100, // 44.1kHz
    var audioType: AudioType = AudioType.MIC, // we have MIC and PCM
    var maxRetry: Int = 3,
    var reconnectDelay: Int = 3000,
) {
    fun getVideoSize() = Streamer.Size(videoHeight, videoWidth)

    fun getAudioType(): AudioConfig.INPUT_TYPE {
        return when (audioType) {
            AudioType.MIC -> AudioConfig.INPUT_TYPE.MIC
            AudioType.PCM -> AudioConfig.INPUT_TYPE.PCM
        }
    }
}