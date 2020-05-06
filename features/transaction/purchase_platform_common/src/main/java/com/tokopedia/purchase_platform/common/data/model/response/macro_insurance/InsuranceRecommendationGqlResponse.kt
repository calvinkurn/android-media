package com.tokopedia.purchase_platform.common.data.model.response.macro_insurance

import com.google.gson.annotations.SerializedName

data class InsuranceRecommendationGqlResponse(
        @SerializedName("insuretechGetTransactionalProducts")
        var data: InsuranceCartResponse = InsuranceCartResponse()
)