package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping

data class ProductsItem(
	val insurance: Insurance? = null,
	val shipperProductId: Int? = null,
	val recommend: Boolean? = null,
	val isShowMap: Int? = null,
	val priority: Int? = null,
	val features: Features? = null,
	val checkSum: String? = null,
	val shipperProductName: String? = null,
	val etd: Etd? = null,
	val texts: Texts? = null,
	val price: Price? = null,
	val shipperProductDesc: String? = null,
	val shipperWeight: Int? = null,
	val shipperId: Int? = null,
	val shipperName: String? = null,
	val status: Int? = null,
	val ut: String? = null
)
