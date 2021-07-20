package com.tokopedia.broadcaster.data

import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer

sealed class AudioType {
    object MIC: AudioType()
    object PCM: AudioType()
}

data class BroadcasterConfig(
    val videoWidth: Int = 1280,
    val videoHeight: Int = 720,
    val fps: Float = 30.0f,
    val videoBitrate: Int = 2000 * 1000, // Kbps -> bps
    val audioRate: Int = 44100, // 44.1kHz
    val audioType: AudioType = AudioType.MIC, // we have MIC and PCM
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