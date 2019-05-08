package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingPartner(
        @SerializedName("data")
        var partnerData: ArrayList<GqlLendingPartnerData>
)