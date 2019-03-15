package com.tokopedia.checkout.domain.datamodel.voucher.promostacking

data class VoucherOrdersItem(
	val cartId: Int? = null,
	val code: String? = null,
	val uniqueId: String? = null,
	val cashbackWalletAmount: Int? = null,
	val success: Boolean? = null,
	val discountAmount: Int? = null,
	val invoiceDescription: String? = null,
	val type: String? = null,
	val message: Message? = null
)
