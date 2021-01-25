package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class TrackHistory(
    @SerializedName("create_time_unix")
    val createTimeUnix: Long = 0,
    @SerializedName("title")
    val title: String = ""
)