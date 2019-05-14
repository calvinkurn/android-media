package com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class KeroAutocompleteGeocode(

	@field:SerializedName("server_process_time")
	val serverProcessTime: String = "",

	@field:SerializedName("data")
	val data: Data = Data(),

	@field:SerializedName("config")
	val config: String = "",

	@field:SerializedName("status")
	val status: String = ""
)