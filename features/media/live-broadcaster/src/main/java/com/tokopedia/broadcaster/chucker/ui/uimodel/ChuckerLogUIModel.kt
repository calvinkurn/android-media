package com.tokopedia.broadcaster.chucker.ui.uimodel

import android.os.Parcelable
import com.tokopedia.broadcaster.data.AudioType
import com.tokopedia.broadcaster.data.BitrateMode
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChuckerLogUIModel(
    var connectionId: Int = 0,
    var startTime: Long = 0,
    var endTime: Long = 0,
    var videoWidth: Int = 0,
    var videoHeight: Int = 0,
    var videoBitrate: Int = 0,
    var audioType: AudioType = AudioType.MIC,
    var audioRate: Int = 0,
    var bitrateMode: BitrateMode = BitrateMode.LadderAscend,
    var ipDevice: String = "",
    var appVersion: String = "",
    var userId: String = "",
) : Parcelable