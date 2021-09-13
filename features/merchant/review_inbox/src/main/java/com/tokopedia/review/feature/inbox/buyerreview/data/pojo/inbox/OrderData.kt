package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderData {
    @SerializedName("invoice_ref_num")
    @Expose
    var invoiceRefNum: String? = null

    @SerializedName("create_time_fmt")
    @Expose
    var createTimeFmt: String? = null

    @SerializedName("invoice_url")
    @Expose
    var invoiceUrl: String? = null
}