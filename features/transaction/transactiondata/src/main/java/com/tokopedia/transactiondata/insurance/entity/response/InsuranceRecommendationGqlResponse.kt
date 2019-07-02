package com.tokopedia.transactiondata.insurance.entity.response

import com.google.gson.annotations.SerializedName

data class InsuranceRecommendationGqlResponse(
        @SerializedName("insuretechGetTransactionalProducts")
        var data: InsuranceCartResponse
)