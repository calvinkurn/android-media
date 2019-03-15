package com.tokopedia.promocheckout.common.view.uimodel

data class VoucherOrdersItemUiModel(
		val success: Boolean? = null,
		val code: String? = null,
		val uniqueId: String? = null,
		val cartId: Int? = null,
		val type: String? = null,
		val cashbackWalletAmount: Int? = null,
		val discountAmount: Int? = null,
		val invoiceDescription: String? = null,
		val message: MessageUiModel? = null
)
