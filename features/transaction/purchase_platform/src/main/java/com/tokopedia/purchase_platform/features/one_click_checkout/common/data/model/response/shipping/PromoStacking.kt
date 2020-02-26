package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping

data class PromoStacking(
	val shipperProductId: Int? = null,
	val promoDetail: String? = null,
	val imageUrl: String? = null,
	val promoTncHtml: String? = null,
	val promoCode: String? = null,
	val title: String? = null,
	val isApplied: Int? = null,
	val discountedRate: Int? = null,
	val benefitAmount: Int? = null,
	val labelInfo: String? = null,
	val shipperDisableText: String? = null,
	val shippingRate: Int? = null,
	val benefitDesc: String? = null,
	val hideShippingName: Boolean? = null,
	val disable: Boolean? = null,
	val serviceId: Int? = null,
	val shipperId: Int? = null,
	val shipperName: String? = null,
	val isPromo: Int? = null,
	val shipperDesc: String? = null
)
