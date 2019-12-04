package com.tokopedia.saldodetails.response.model.saldoholdinfo.response

import com.google.gson.annotations.SerializedName

data class BuyerDataItem(

	@SerializedName("hold_date")
	val holdDate: String? = null,

	@SerializedName("amount_fmt")
	val amountFmt: String? = null,

	@SerializedName("reason")
	val reason: String? = null,

	@SerializedName("reason_title")
	val reasonTitle: String? = null,

	@SerializedName("release_date")
	val releaseDate: String? = null,

	@SerializedName("invoice")
	val invoice: String? = null,

	@SerializedName("type")
	val type: Int? = null
)