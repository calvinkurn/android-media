package com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class PhotosItem(

	@field:SerializedName("photo_reference")
	val photoReference: String = "",

	@field:SerializedName("width")
	val width: Int = 0,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<String> = emptyList(),

	@field:SerializedName("height")
	val height: Int = 0
)