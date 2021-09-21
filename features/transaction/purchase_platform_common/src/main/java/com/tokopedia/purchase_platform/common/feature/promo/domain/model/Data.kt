package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class Data(

		@SerializedName("codes")
		val codes: List<String?>? = null,

		@SerializedName("voucher_orders")
		val voucherOrders: List<VoucherOrdersItem?>? = null,

		@SerializedName("cashback_advocate_referral_amount")
		val cashbackAdvocateReferralAmount: Int? = null,

		@SerializedName("clashing_info_detail")
		val clashingInfoDetail: ClashingInfoDetail? = null,

		@SerializedName("cashback_wallet_amount")
		val cashbackWalletAmount: Int? = null,

		@SerializedName("discount_amount")
		val discountAmount: Int? = null,

		@SerializedName("title_description")
		val titleDescription: String? = null,

		@SerializedName("is_tokopedia_gerai")
		val isTokopediaGerai: Boolean? = null,

		@SerializedName("global_success")
		val globalSuccess: Boolean? = null,

		@SerializedName("tracking_details")
		val trackingDetails: List<TrackingDetailsItem?>? = null,

		@SerializedName("message")
		val message: Message? = null,

		@SerializedName("gateway_id")
		val gatewayId: String? = null,

		@SerializedName("is_coupon")
		val isCoupon: Int? = null,

		@SerializedName("additional_info")
		val additionalInfo: AdditionalInfo? = null,

		@SerializedName("success")
		val success: Boolean? = null,

		@SerializedName("invoice_description")
		val invoiceDescription: String? = null,

		@SerializedName("benefit_summary_info")
		val benefitSummaryInfo: BenefitSummaryInfo? = null,

		@SerializedName("ticker_info")
		val tickerInfo: TickerInfo? = null
)