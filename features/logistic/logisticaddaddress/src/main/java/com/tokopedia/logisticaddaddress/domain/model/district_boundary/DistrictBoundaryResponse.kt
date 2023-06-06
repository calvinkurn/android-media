package com.tokopedia.logisticaddaddress.domain.model.district_boundary

import com.google.gson.annotations.SerializedName

data class DistrictBoundaryResponse(

	@SerializedName("keroGetDistrictBoundaryArray")
	val keroGetDistrictBoundaryArray: KeroGetDistrictBoundaryArray = KeroGetDistrictBoundaryArray()
)
