package com.tokopedia.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.BenefitSummaryInfo

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class LastApplyPromoData(
        @SerializedName("message")
        var message: MessageGlobalPromo = MessageGlobalPromo(),

        @SerializedName("codes")
        var codes: List<String> = emptyList(),

        @SerializedName("voucher_orders")
        var listVoucherOrders: List<VoucherOrders> = emptyList(),

        @SerializedName("additional_info")
        var additionalInfo: PromoAdditionalInfo = PromoAdditionalInfo(),

        @SerializedName("tracking_details")
        var trackingDetails: List<TrackingDetail> = emptyList(),

        @SerializedName("benefit_summary_info")
        var benefitSummaryInfo: BenefitSummaryInfo = BenefitSummaryInfo()

)