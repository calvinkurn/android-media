package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import com.google.gson.annotations.SerializedName

data class DistrictZipcodes(
        @field:SerializedName("keroGetDistrictDetails")
        val keroDistrictDetails: KeroDistrictRecommendation = KeroDistrictRecommendation()
)