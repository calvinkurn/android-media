package com.tokopedia.logisticaddaddress.domain.model.district_boundary

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<List<List<List<Double>>>> = emptyList()
)