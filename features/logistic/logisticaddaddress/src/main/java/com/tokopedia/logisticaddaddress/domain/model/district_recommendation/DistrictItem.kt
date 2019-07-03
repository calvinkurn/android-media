package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class DistrictItem(

	@field:SerializedName("district_name")
	val districtName: String = "",

	@field:SerializedName("city_name")
	val cityName: String = "",

	@field:SerializedName("province_id")
	val provinceId: Int = 0,

	@field:SerializedName("district_id")
	val districtId: Int = 0,

	@field:SerializedName("zip_code")
	val zipCode: List<String> = emptyList(),

	@field:SerializedName("city_id")
	val cityId: Int = 0,

	@field:SerializedName("province_name")
	val provinceName: String = ""
)