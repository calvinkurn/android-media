package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopupBillsEnquiryQuery (
        @SerializedName("Key")
        @Expose
        val key: String = "",
        @SerializedName("Value")
        @Expose
        val value: String = ""
)