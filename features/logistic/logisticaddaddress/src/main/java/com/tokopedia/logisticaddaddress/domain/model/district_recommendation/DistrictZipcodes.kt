package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import com.google.gson.annotations.SerializedName

data class DistrictZipcodes(
        @field:SerializedName("kero_get_district_details")
        val keroDistrictDetails: KeroDistrictRecommendation = KeroDistrictRecommendation()
)