package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping

data class Response(
	val promoStacking: PromoStacking? = null,
	val id: String? = null,
	val services:ArrayList<ServicesItem>,
	val type: String? = null,
	val ratesId: Long? = null
)
