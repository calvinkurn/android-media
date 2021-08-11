package com.tokopedia.saldodetails.response.model.saldoholdinfo.response

import com.google.gson.annotations.SerializedName

data class SaldoHoldDepositHistory(

	@SerializedName("seller_data")
	val sellerData: ArrayList<SaldoHoldInfoItem?>? = null,

	@SerializedName("total")
	val total: Double? = null,

	@SerializedName("code")
	val code: Int? = null,

	@SerializedName("ticker_message_isshow")
	val tickerMessageIsshow: Boolean? = null,

	@SerializedName("ticker_message_id")
	val tickerMessageId: String? = null,

	@SerializedName("ticker_message_en")
	val tickerMessageEn: String? = null,

	@SerializedName("error")
	val error: Boolean? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("total_fmt")
	val totalFmt: String? = null,

	@SerializedName("buyer_data")
	val buyerData: ArrayList<SaldoHoldInfoItem?>? = null
)
