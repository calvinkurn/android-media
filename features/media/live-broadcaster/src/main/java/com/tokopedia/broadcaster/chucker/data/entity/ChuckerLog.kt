package com.tokopedia.broadcaster.chucker.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.broadcaster.data.AudioType
import com.tokopedia.broadcaster.data.BitrateMode

@Entity(tableName = "ChuckerLog")
data class ChuckerLog(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "connectionId")
    var connectionId: Int = 0,

    @ColumnInfo(name = "startTime")
    var startTime: Long = 0,

    @ColumnInfo(name = "endTime")
    var endTime: Long = 0,

    @ColumnInfo(name = "videoWidth")
    var videoWidth: Int = 0,

    @ColumnInfo(name = "videoHeight")
    var videoHeight: Int = 0,

    @ColumnInfo(name = "videoBitrate")
    var videoBitrate: Int = 0,

    @ColumnInfo(name = "audioType")
    var audioType: AudioType = AudioType.MIC,

    @ColumnInfo(name = "audioRate")
    var audioRate: Int = 0,

    @ColumnInfo(name = "bitrateMode")
    var bitrateMode: BitrateMode = BitrateMode.LadderAscend,

    @ColumnInfo(name = "ipDevice")
    var ipDevice: String = "",

    @ColumnInfo(name = "appVersion")
    var appVersion: String = "",

    @ColumnInfo(name = "userId")
    var userId: String = "",

)