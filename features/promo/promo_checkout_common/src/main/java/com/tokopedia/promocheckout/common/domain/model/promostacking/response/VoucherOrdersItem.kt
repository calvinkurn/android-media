package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class VoucherOrdersItem(

	@field:SerializedName("cart_id")
	val cartId: Int? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("unique_id")
	val uniqueId: String? = null,

	@field:SerializedName("cashback_wallet_amount")
	val cashbackWalletAmount: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("discount_amount")
	val discountAmount: Int? = null,

	@field:SerializedName("invoice_description")
	val invoiceDescription: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("message")
	val message: Message? = null
)