package com.tokopedia.logisticaddaddress.domain.model.get_district

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class FullDataItem(

	@field:SerializedName("types")
	val types: List<String> = emptyList(),

	@field:SerializedName("short_name")
	val shortName: String = "",

	@field:SerializedName("long_name")
	val longName: String = ""
)