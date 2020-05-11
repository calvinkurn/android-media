package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

data class Address(
        val latitude: String? = null,
        val addressId: Int? = null,
        val addressName: String? = null,
        val provinceName: String? = null,
        val districtName: String? = null,
        val cityName: String? = null,
        val provinceId: Int? = null,
        val phone: String? = null,
        val addressStreet: String? = null,
        val receiverName: String? = null,
        val districtId: Int? = null,
        val postalCode: String? = null,
        val cityId: Int? = null,
        val longitude: String? = null
)