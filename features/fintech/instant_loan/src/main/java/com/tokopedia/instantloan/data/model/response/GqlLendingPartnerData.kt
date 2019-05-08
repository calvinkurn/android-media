package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingPartnerData(
        @SerializedName("ID")
        var partnerId: Int,

        @SerializedName("Name")
        var partnerName: String,

        @SerializedName("NameSlug")
        var partnerNameSlug: String,

        @SerializedName("Logo")
        var partnerIconUrl: String
)