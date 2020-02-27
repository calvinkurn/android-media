package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping

data class ServicesItem(
	val texts: Texts? = null,
	val serviceName: String? = null,
	val serviceId: Int? = null,
	val rangePrice: RangePrice? = null,
	val status: Int? = null,
	val products: List<ProductsItem?>? = null
)
