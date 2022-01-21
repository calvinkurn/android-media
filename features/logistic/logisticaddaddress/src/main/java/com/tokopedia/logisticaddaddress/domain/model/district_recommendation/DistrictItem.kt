package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import android.annotation.SuppressLint
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class DistrictItem(

	@field:SerializedName("district_name")
	val districtName: String = "",

	@field:SerializedName("city_name")
	val cityName: String = "",

	@SuppressLint("Invalid Data Type")
	@field:SerializedName("province_id")
	val provinceId: Long = 0,

	@SuppressLint("Invalid Data Type")
	@field:SerializedName("district_id")
	val districtId: Long = 0,

	@field:SerializedName("zip_code")
	val zipCode: List<String> = emptyList(),

	@SuppressLint("Invalid Data Type")
	@field:SerializedName("city_id")
	val cityId: Long = 0,

	@field:SerializedName("province_name")
	val provinceName: String = ""
)