package com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Geometry(

		@field:SerializedName("types")
	val types: List<Any> = emptyList(),

		@field:SerializedName("viewport")
	val viewport: Viewport = Viewport(),

		@field:SerializedName("bounds")
	val bounds: Bounds = Bounds(),

		@field:SerializedName("location")
	val location: Location = Location(),

		@field:SerializedName("location_type")
	val locationType: String = ""
)