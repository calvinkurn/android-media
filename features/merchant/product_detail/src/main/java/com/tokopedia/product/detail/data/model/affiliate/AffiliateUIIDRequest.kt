package com.tokopedia.product.detail.data.model.affiliate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AffiliateUIIDRequest (
        @SerializedName("uuid")
        @Expose
        val uuid: String = "",
        @SerializedName("trackerID")
        @Expose
        val trackerID: String = "",
        @SerializedName("irisSessionID")
        @Expose
        val irisSessionID: String = ""
        )