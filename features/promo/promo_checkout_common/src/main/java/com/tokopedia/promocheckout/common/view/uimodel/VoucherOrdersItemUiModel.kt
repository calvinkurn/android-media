package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VoucherOrdersItemUiModel(
		var success: Boolean = false,
		var code: String = "",
		var uniqueId: String = "",
		var cartId: String = "-1",
		var type: String = "",
		var cashbackWalletAmount: Long = -1L,
		var discountAmount: Long = -1L,
		var invoiceDescription: String = "",
		var titleDescription: String = "",
		var message: MessageUiModel = MessageUiModel()
) : Parcelable
