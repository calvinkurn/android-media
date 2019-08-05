package com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class OpeningHours(

	@field:SerializedName("open_now")
	val openNow: Boolean = false,

	@field:SerializedName("permanently_closed")
	val permanentlyClosed: Any = Any(),

	@field:SerializedName("periods")
	val periods: List<Any> = emptyList(),

	@field:SerializedName("weekday_text")
	val weekdayText: List<Any> = emptyList()
)