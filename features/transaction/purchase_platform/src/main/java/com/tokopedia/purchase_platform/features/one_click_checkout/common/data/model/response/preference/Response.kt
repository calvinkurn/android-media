package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference

data class Response(
	val success: Int? = null,
	val profiles: ArrayList<ProfilesItem>,
	val messages: List<Any?>? = null
)
