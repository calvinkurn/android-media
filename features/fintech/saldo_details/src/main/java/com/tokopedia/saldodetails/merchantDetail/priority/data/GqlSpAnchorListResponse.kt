package com.tokopedia.saldodetails.merchantDetail.priority.data

import com.google.gson.annotations.SerializedName


data class GqlSpAnchorListResponse(
        @SerializedName("label")
        var label: String? = null,

        @SerializedName("url")
        var url: String? = null,

        @SerializedName("color")
        var color: String? = null
)
