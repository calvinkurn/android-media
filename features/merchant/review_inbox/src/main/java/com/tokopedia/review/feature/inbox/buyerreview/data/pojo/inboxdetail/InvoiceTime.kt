package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InvoiceTime {
    @SerializedName("date_time_fmt1")
    @Expose
    var dateTimeFmt1: String? = null

    @SerializedName("date_time_fmt1x")
    @Expose
    var dateTimeFmt1x: String? = null

    @SerializedName("date_time_fmt2")
    @Expose
    var dateTimeFmt2: String? = null

    @SerializedName("date_time_fmt3")
    @Expose
    var dateTimeFmt3: String? = null

    @SerializedName("date_time_fmt3x")
    @Expose
    var dateTimeFmt3x: String? = null

    @SerializedName("date_fmt1")
    @Expose
    var dateFmt1: String? = null
}