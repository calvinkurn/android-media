package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference

data class PaymentModel(
	var image: String? = null,
	var description: String? = null,
	var gatewayCode: String? = null,
	var url: String? = null,
	var gatewayName: String? = null,
	var metadata: String? = null
)
