package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName

data class PromoValidateUseResponse(

        @field:SerializedName("codes")
        val codes: List<String> = emptyList(),

        @field:SerializedName("voucher_orders")
        val voucherOrders: List<VoucherOrdersItem> = emptyList(),

        @field:SerializedName("clashing_info_detail")
        val clashingInfoDetail: ClashingInfoDetail = ClashingInfoDetail(),

        @field:SerializedName("title_description")
        val titleDescription: String = "",

        @field:SerializedName("is_tokopedia_gerai")
        val isTokopediaGerai: Boolean = false,

        @field:SerializedName("global_success")
        val globalSuccess: Boolean = false,

        @field:SerializedName("tracking_details")
        val trackingDetails: List<TrackingDetailsItem> = emptyList(),

        @field:SerializedName("message")
        val message: Message = Message(),

        @field:SerializedName("gateway_id")
        val gatewayId: String = "",

        @field:SerializedName("is_coupon")
        val isCoupon: Int = 0,

        @field:SerializedName("coupon_description")
        val couponDescription: String = "",

        @field:SerializedName("additional_info")
        val additionalInfo: AdditionalInfo = AdditionalInfo(),

        @field:SerializedName("success")
        val success: Boolean = false,

        @field:SerializedName("invoice_description")
        val invoiceDescription: String = "",

        @field:SerializedName("benefit_summary_info")
        val benefitSummaryInfo: BenefitSummaryInfo = BenefitSummaryInfo(),

        @field:SerializedName("ticker_info")
        val tickerInfo: TickerInfo = TickerInfo(),

        @field:SerializedName("cashback_voucher_description")
        val cashbackVoucherDescription: String = "",

        @field:SerializedName("tokopoints_detail")
        val tokopointsDetail: TokopointsDetail = TokopointsDetail()
)