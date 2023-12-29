package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import com.google.gson.annotations.SerializedName

data class DistrictRecommendationResponse(

    @SerializedName("kero_district_recommendation")
    val keroDistrictRecommendation: KeroDistrictRecommendation = KeroDistrictRecommendation()
)
