package com.tokopedia.affiliate.feature.dashboard.data.pojo.commission

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class AffiliateHistoryPojo(

	@SerializedName("tkpdCommissionFormatted")
	@Expose
	val tkpdCommissionFormatted: String = "",

	@SerializedName("tkpdCommission")
	@Expose
	val tkpdCommission: Int = 0,

	@SerializedName("affCommission")
	@Expose
	val affCommission: Int = 0,

	@SerializedName("itemSent")
	@Expose
	val itemSent: Int = 0,

	@SerializedName("txTime")
	@Expose
	val txTime: String = "",

	@SerializedName("tkpdInvoice")
	@Expose
	val tkpdInvoice: String = "",

	@SerializedName("affCommissionFormatted")
	@Expose
	val affCommissionFormatted: String = "",

	@SerializedName("txTimeFormatted")
	@Expose
	val txTimeFormatted: String = "",

	@SerializedName("netCommission")
	@Expose
	val netCommission: Int = 0,

	@SerializedName("affInvoiceURL")
	@Expose
	val affInvoiceURL: String = "",

	@SerializedName("affInvoice")
	@Expose
	val affInvoice: String = "",

	@SerializedName("tkpdInvoiceURL")
	@Expose
	val tkpdInvoiceURL: String = "",

	@SerializedName("netCommissionFormatted")
	@Expose
	val netCommissionFormatted: String = ""
)