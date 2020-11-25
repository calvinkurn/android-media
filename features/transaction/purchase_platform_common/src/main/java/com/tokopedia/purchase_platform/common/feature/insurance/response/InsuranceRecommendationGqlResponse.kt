package com.tokopedia.purchase_platform.common.feature.insurance.response

import com.google.gson.annotations.SerializedName

data class InsuranceRecommendationGqlResponse(
        @SerializedName("insuretechGetTransactionalProducts")
        var data: InsuranceCartResponse = InsuranceCartResponse()
)