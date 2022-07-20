package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderData(
    @SerializedName("invoice_ref_num")
    @Expose
    val invoiceRefNum: String = "",

    @SerializedName("create_time_fmt")
    @Expose
    val createTimeFmt: String = "",

    @SerializedName("invoice_url")
    @Expose
    val invoiceUrl: String = ""
)