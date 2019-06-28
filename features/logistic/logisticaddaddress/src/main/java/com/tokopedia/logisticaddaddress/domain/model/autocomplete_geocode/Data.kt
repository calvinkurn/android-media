package com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Data(

	@field:SerializedName("Results")
	val results: List<ResultsItem> = emptyList(),

	@field:SerializedName("NextPageToken")
	val nextPageToken: String = "",

	@field:SerializedName("HTMLAttributions")
	val hTMLAttributions: List<Any> = emptyList()
)