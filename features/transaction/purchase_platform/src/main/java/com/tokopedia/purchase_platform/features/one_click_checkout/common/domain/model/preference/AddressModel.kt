package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference

data class AddressModel(
	var latitude: String? = null,
	var addressId: Int? = null,
	var addressName: String? = null,
	var provinceName: String? = null,
	var districtName: String? = null,
	var cityName: String? = null,
	var provinceId: Int? = null,
	var phone: String? = null,
	var addressStreet: String? = null,
	var receiverName: String? = null,
	var districtId: Int? = null,
	var postalCode: String? = null,
	var cityId: Int? = null,
	var longitude: String? = null,
	var fullAddress: String? = null
)
