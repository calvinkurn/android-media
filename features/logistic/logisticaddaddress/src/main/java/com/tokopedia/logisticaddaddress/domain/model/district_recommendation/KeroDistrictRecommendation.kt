package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class KeroDistrictRecommendation(

	@field:SerializedName("district")
	val district: List<DistrictItem> = emptyList(),

	@field:SerializedName("next_available")
	val nextAvailable: Boolean = false
)