package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class Data(

		@SerializedName("codes")
		val codes: List<String?>? = null,

		@SerializedName("voucher_orders")
		val voucherOrders: List<VoucherOrdersItem?>? = null,

		@SerializedName("tracking_details")
		val trackingDetails: List<TrackingDetailsItem?>? = null,

		@SerializedName("message")
		val message: Message? = null,

		@SerializedName("additional_info")
		val additionalInfo: AdditionalInfo? = null,

		@SerializedName("success")
		val success: Boolean? = null
)