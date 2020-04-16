package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping

data class OntimeDeliveryGuarantee(
	val textLabel: String? = null,
	val iconUrl: String? = null,
	val urlText: String? = null,
	val bom: Bom? = null,
	val textDetail: String? = null,
	val urlDetail: String? = null,
	val available: Boolean? = null,
	val value: Int? = null
)
