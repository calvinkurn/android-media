package com.tokopedia.product.detail.data.model.affiliate

import com.google.gson.annotations.SerializedName

data class AffiliateUIIDRequest (
        @SerializedName("uuid")
        val uuid: String = "",
        @SerializedName("trackerID")
        val trackerID: String = "",
        @SerializedName("irisSessionID")
        val irisSessionID: String = ""
        )