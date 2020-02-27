package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference

data class ProfilesItem(
	val address: Address? = null,
	val shipment: Shipment? = null,
	val profileId: Int? = null,
	val payment: Payment? = null,
	val status: Int? = null
)
