package com.tokopedia.promocheckout.common.view.uimodel

data class VoucherOrdersItemUiModel(
		var success: Boolean = false,
		var code: String = "",
		var uniqueId: String = "",
		var cartId: Int = -1,
		var type: String = "",
		var cashbackWalletAmount: Int = -1,
		var discountAmount: Int = -1,
		var invoiceDescription: String = "",
		var message: MessageUiModel = MessageUiModel()
)
