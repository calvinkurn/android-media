package com.tokopedia.homenav.mainnav.data.pojo.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Order(
        @SerializedName("metadata")
        @Expose
        val metadata: Metadata? = Metadata(),
        @SerializedName("orderUUID")
        @Expose
        val orderUUID: String? = "",
        @SerializedName("searchableText")
        @Expose
        val searchableText: String? = "",
        @SerializedName("status")
        @Expose
        val status: String? = "",
        @SerializedName("userID")
        @Expose
        val userID: String? = "",
        @SerializedName("verticalCategory")
        @Expose
        val verticalCategory: String? = "",
        @SerializedName("verticalID")
        @Expose
        val verticalID: String? = "",
        @SerializedName("verticalStatus")
        @Expose
        val verticalStatus: String? = ""
)