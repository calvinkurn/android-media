package com.tokopedia.buyerorder.detail.data.getcancellationreason

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuyerGetCancellationReasonParam(

	@SerializedName("user_id")
	@Expose
	val userId: String = "",

	@SerializedName("order_id")
	val orderId: String = ""
)