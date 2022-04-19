package com.tokopedia.broadcaster.data.uimodel

import android.os.Parcelable
import com.tokopedia.broadcaster.data.AudioType
import com.tokopedia.broadcaster.data.BitrateMode
import kotlinx.android.parcel.Parcelize

/*
* This class are used on stats nerd and for logging
* that's why we need this uiModel on production
* */

@Parcelize
data class LoggerUIModel(
    var id: Int = 0,
    var url: String = "",
    var connectionId: Int = 0,
    var startTime: Long = 0,
    var endTime: Long = 0,
    var videoWidth: Int = 0,
    var videoHeight: Int = 0,
    var videoBitrate: Int = 0,
    var audioType: AudioType = AudioType.MIC,
    var audioRate: Int = 0,
    var audioBitrate: Int = 0,
    var bitrateMode: BitrateMode = BitrateMode.LadderAscend,
    var appVersion: String = "",
    var userId: String = "",
    var fps: String = "",
    var bandwidth: String = "",
    var traffic: String = "",
    var isPacketLossIncreasing: Boolean = false,
) : Parcelable