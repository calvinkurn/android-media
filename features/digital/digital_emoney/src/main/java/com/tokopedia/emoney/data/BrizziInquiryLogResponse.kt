package com.tokopedia.emoney.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BrizziInquiryLogResponse(
        @SerializedName("rechargeEmoneyInquiryLog")
        @Expose
        val emoneyInquiryLog: BrizziInquiryLog? = null
)

class BrizziInquiryLog(
        @SerializedName("inquiry_id")
        @Expose
        val inquiryId: Int = 0,
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("message")
        @Expose
        val message: String = ""
)