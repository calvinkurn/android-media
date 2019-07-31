package com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Bounds(

		@field:SerializedName("southwest")
	val southwest: Southwest = Southwest(),

		@field:SerializedName("northeast")
	val northeast: Northeast = Northeast()
)