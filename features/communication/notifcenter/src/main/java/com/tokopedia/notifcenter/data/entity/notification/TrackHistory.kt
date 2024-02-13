package com.tokopedia.notifcenter.data.entity.notification

import com.google.gson.annotations.SerializedName

class TrackHistory(
    @SerializedName("create_time_unix")
    val createTimeUnix: Long = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("title_html")
    val titleHtml: String = "",
    @SerializedName("description_html")
    val description: String = ""
) {
    val createTimeUnixMillis: Long get() = createTimeUnix * 1000
}
