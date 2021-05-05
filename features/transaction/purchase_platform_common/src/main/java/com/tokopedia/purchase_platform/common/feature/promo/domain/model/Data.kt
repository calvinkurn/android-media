package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class Data(
		@field:SerializedName("codes")
		val codes: List<String?>? = null,

		@field:SerializedName("voucher_orders")
		val voucherOrders: List<VoucherOrdersItem?>? = null,

		@field:SerializedName("tracking_details")
		val trackingDetails: List<TrackingDetailsItem?>? = null,

		@field:SerializedName("message")
		val message: Message? = null,

		@field:SerializedName("additional_info")
		val additionalInfo: AdditionalInfo? = null,

		@field:SerializedName("success")
		val success: Boolean? = null,

		@field:SerializedName("benefit_summary_info")
		val benefitSummaryInfo: BenefitSummaryInfo? = null
)