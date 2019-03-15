package com.tokopedia.promocheckout.common.view.uimodel

data class DataUiModel(
		val globalSuccess: Boolean? = null,
		val success: Boolean? = null,
		val message: MessageUiModel? = null,
		val promoCodeId: Int? = null,
		val codes: List<String?>? = null,
		val titleDescription: String? = null,
		val discountAmount: Int? = null,
		val cashbackWalletAmount: Int? = null,
		val cashbackAdvocateReferralAmount: Int? = null,
		val cashbackVoucherDescription: String? = null,
		val invoiceDescription: String? = null,
		val gatewayId: String? = null,
		val isCoupon: Int? = null,
		val couponDescription: String? = null,
		val voucherOrders: List<VoucherOrdersItemUiModel>? = null
)
