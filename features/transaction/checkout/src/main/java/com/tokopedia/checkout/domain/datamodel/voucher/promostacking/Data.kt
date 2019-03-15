package com.tokopedia.checkout.domain.datamodel.voucher.promostacking

data class Data(
	var codes: List<String?>? = null,
	var promoCodeId: Int? = null,
	var voucherOrders: List<VoucherOrdersItem?>? = null,
	var cashbackAdvocateReferralAmount: Int? = null,
	var cashbackWalletAmount: Int? = null,
	var discountAmount: Int? = null,
	var titleDescription: String? = null,
	var globalSuccess: Boolean? = null,
	var message: Message? = null,
	var gatewayId: String? = null,
	var isCoupon: Int? = null,
	var couponDescription: String? = null,
	var success: Boolean? = null,
	var invoiceDescription: String? = null,
	var cashbackVoucherDescription: String? = null
)
