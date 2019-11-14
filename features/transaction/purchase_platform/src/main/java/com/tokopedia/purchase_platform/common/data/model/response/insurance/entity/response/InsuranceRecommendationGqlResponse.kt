package com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartResponse

data class InsuranceRecommendationGqlResponse(
        @SerializedName("insuretechGetTransactionalProducts")
        var data: InsuranceCartResponse = InsuranceCartResponse()
)