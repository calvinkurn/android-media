package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.shipping

data class Insurance(
	val insuranceType: Int? = null,
	val insuranceUsedDefault: Int? = null,
	val insuranceUsedInfo: String? = null,
	val insuranceActualPrice: Int? = null,
	val insurancePrice: Int? = null,
	val insuranceTypeInfo: String? = null,
	val insuranceUsedType: Int? = null,
	val insurancePayType: Int? = null
)
