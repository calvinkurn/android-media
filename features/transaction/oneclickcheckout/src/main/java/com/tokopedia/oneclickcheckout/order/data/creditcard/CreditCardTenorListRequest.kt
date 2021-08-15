package com.tokopedia.oneclickcheckout.order.data.creditcard

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CreditCardTenorListRequest(

	@field:SerializedName("token_id")
	val tokenId: String = "",

	@SuppressLint("Invalid Data Type")
	@field:SerializedName("user_id")
	val userId: Long = 0L,

	@field:SerializedName("total_amount")
	val totalAmount: Float = 0F,

	@field:SerializedName("discount_amount")
	val discountAmount: Float = 0F,

	@field:SerializedName("profile_code")
	val profileCode: String = "",

	@field:SerializedName("other_amount")
	val otherAmount: Float = 0F,

	@field:SerializedName("cart_details")
	val cartDetails: List<CartDetailsItem> = emptyList(),

	@field:SerializedName("ccfee_signature")
	val ccfeeSignature: String = "",

	@field:SerializedName("timestamp")
	val timestamp: String = ""
)

data class CartDetailsItem(

	@field:SerializedName("shop_type")
	val shopType: Int = 0,

	@field:SerializedName("payment_amount")
	val paymentAmount: Float = 0F
)
