package com.tokopedia.oneclickcheckout.order.data.creditcard

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CreditCardTenorListRequest(

	@SerializedName("token_id")
	val tokenId: String = "",

	@SuppressLint("Invalid Data Type")
	@SerializedName("user_id")
	val userId: Long = 0L,

	@SerializedName("total_amount")
	val totalAmount: Double = 0.0,

	@SerializedName("discount_amount")
	val discountAmount: Double = 0.0,

	@SerializedName("profile_code")
	val profileCode: String = "",

	@SerializedName("other_amount")
	val otherAmount: Double = 0.0,

	@SerializedName("cart_details")
	val cartDetails: List<CartDetailsItem> = emptyList(),

	@SerializedName("ccfee_signature")
	val ccfeeSignature: String = "",

	@SerializedName("timestamp")
	val timestamp: String = ""
)

data class CartDetailsItem(

	@SerializedName("shop_type")
	val shopType: Int = 0,

	@SerializedName("payment_amount")
	val paymentAmount: Double = 0.0
)
