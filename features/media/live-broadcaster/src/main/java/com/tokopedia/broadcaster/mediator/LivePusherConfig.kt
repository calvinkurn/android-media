package com.tokopedia.broadcaster.mediator

import com.tokopedia.broadcaster.data.AudioType
import com.tokopedia.broadcaster.data.BitrateMode

interface LivePusherConfig {
    var videoWidth: Int
    var videoHeight: Int
    var fps: Float
    var videoBitrate: Int
    var audioRate: Int
    var audioChannelCount: Int
    var audioSource: Int
    var audioType: AudioType
    var maxRetry: Int
    var reconnectDelay: Int
    var bitrateMode: BitrateMode
    var netTrackerInterval: Int
    val audioBitrate: Int
}