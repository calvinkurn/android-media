package com.tokopedia.promocheckout.common.view.uimodel

data class DataUiModel(
		var globalSuccess: Boolean = false,
		var success: Boolean = false,
		var message: MessageUiModel = MessageUiModel(),
		var promoCodeId: Int = -1,
		var codes: List<String> = emptyList(),
		var titleDescription: String = "",
		var discountAmount: Int = -1,
		var cashbackWalletAmount: Int = -1,
		var cashbackAdvocateReferralAmount: Int = -1,
		var cashbackVoucherDescription: String = "",
		var invoiceDescription: String = "",
		var gatewayId: String = "",
		var isCoupon: Int = -1,
		var couponDescription: String = "",
		var clashings: ClashingInfoDetailUiModel = ClashingInfoDetailUiModel(),
		var voucherOrders: List<VoucherOrdersItemUiModel> = emptyList()
)
