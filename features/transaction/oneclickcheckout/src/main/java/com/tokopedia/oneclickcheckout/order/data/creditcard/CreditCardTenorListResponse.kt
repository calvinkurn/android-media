package com.tokopedia.oneclickcheckout.order.data.creditcard

import com.google.gson.annotations.SerializedName

data class CreditCardTenorListResponse(
	@field:SerializedName("creditCardTenorList")
	val response: CreditCardTenorList = CreditCardTenorList()
)

data class CreditCardTenorList(
	@field:SerializedName("error_msg")
	val errorMsg: String = "",

	@field:SerializedName("error_code")
	val errorCode: String = "",

	@field:SerializedName("tenor_list")
	val tenorList: List<TenorListItem> = emptyList(),

	@field:SerializedName("process_time")
	val processTime: String = ""
)

data class TenorListItem(

	@field:SerializedName("amount")
	val amount: Float = 0F,

	@field:SerializedName("bank")
	val bank: String,

	@field:SerializedName("rate")
	val rate: Float = 0F,

	@field:SerializedName("fee")
	val fee: Float = 0F,

	@field:SerializedName("description")
	val description: String = "",

	@field:SerializedName("disabled")
	val disabled: Boolean = false,

	@field:SerializedName("type")
	val type: String = ""
)
