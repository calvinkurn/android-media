package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.broadcaster.data.AudioType
import com.tokopedia.broadcaster.data.BitrateMode
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.mediator.LivePusherConfig
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer


/**
 * Created by mzennis on 03/06/21.
 */
data class PlayLivePusherConfig(
    override var videoWidth: Int = 1280,
    override var videoHeight: Int = 720,
    override var fps: Float = 30.0f,
    override var videoBitrate: Int = 2000 * 1000, // Kbps -> bps
    override var audioRate: Int = 44100,
    override var audioChannelCount: Int = 1,
    override var audioSource: Int = 5,
    override var netTrackerInterval: Int = 5000,
    override val audioBitrate: Int = AudioConfig.calcBitRate(audioRate, audioChannelCount, AudioConfig.AAC_PROFILE),
): LivePusherConfig {

    fun getVideoSize() = Streamer.Size(videoHeight, videoWidth)

    private val broadcasterConfig = BroadcasterConfig()

    override var audioType: AudioType = broadcasterConfig.audioType

    override var maxRetry: Int = broadcasterConfig.maxRetry

    override var reconnectDelay: Int = broadcasterConfig.reconnectDelay

    override var bitrateMode: BitrateMode = broadcasterConfig.bitrateMode

}