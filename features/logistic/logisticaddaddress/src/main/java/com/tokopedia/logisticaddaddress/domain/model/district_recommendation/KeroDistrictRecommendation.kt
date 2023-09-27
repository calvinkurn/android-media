package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import com.google.gson.annotations.SerializedName

data class KeroDistrictRecommendation(

    @SerializedName("district")
    val district: List<DistrictItem> = emptyList(),

    @SerializedName("next_available")
    val nextAvailable: Boolean = false
)
