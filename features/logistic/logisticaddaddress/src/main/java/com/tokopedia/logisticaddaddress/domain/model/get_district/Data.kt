package com.tokopedia.logisticaddaddress.domain.model.get_district

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Data(

	@field:SerializedName("district_name")
	val districtName: String = "",

	@field:SerializedName("formatted_address")
	val formattedAddress: String = "",

	@field:SerializedName("province_id")
	val provinceId: Int = 0,

	@field:SerializedName("latitude")
	val latitude: String = "",

	@field:SerializedName("full_data")
	val fullData: List<FullDataItem> = emptyList(),

	@field:SerializedName("district_id")
	val districtId: Int = 0,

	@field:SerializedName("title")
	val title: String = "",

	@field:SerializedName("postal_code")
	val postalCode: String = "",

	@field:SerializedName("city_id")
	val cityId: Int = 0,

	@field:SerializedName("longitude")
	val longitude: String = ""
)