package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("codes")
    val codes: List<String> = emptyList(),

    @SerializedName("voucher_orders")
    val voucherOrders: List<VoucherOrdersItem> = emptyList(),

    @SerializedName("cashback_advocate_referral_amount")
    val cashbackAdvocateReferralAmount: Int = 0,

    @SerializedName("clashing_info_detail")
    val clashingInfoDetail: ClashingInfoDetail = ClashingInfoDetail(),

    @SerializedName("cashback_wallet_amount")
    val cashbackWalletAmount: Int = 0,

    @SerializedName("discount_amount")
    val discountAmount: Int = 0,

    @SerializedName("title_description")
    val titleDescription: String = "",

    @SerializedName("is_tokopedia_gerai")
    val isTokopediaGerai: Boolean = false,

    @SerializedName("global_success")
    val globalSuccess: Boolean = false,

    @SerializedName("tracking_details")
    val trackingDetails: List<TrackingDetailsItem> = emptyList(),

    @SerializedName("message")
    val message: Message = Message(),

    @SerializedName("gateway_id")
    val gatewayId: String = "",

    @SerializedName("is_coupon")
    val isCoupon: Int = 0,

    @SerializedName("additional_info")
    val additionalInfo: AdditionalInfo = AdditionalInfo(),

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("invoice_description")
    val invoiceDescription: String = "",

    @SerializedName("benefit_summary_info")
    val benefitSummaryInfo: BenefitSummaryInfo = BenefitSummaryInfo(),

    @SerializedName("ticker_info")
    val tickerInfo: TickerInfo = TickerInfo()
)
