package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCreateTime {
    @SerializedName("date_time_fmt1")
    @Expose
    var dateTimeFmt1: String? = null

    @SerializedName("unix_timestamp")
    @Expose
    var unixTimestamp: String? = null

    @SerializedName("date_time_ios")
    @Expose
    var dateTimeIos: String? = null

    @SerializedName("date_time_android")
    @Expose
    var dateTimeAndroid: String? = null
}