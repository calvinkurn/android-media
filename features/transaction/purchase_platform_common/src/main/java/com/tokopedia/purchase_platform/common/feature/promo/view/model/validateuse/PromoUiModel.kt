package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoUiModel(
    var codes: List<String> = listOf(),
    var promoCodeId: Int = -1,
    var voucherOrderUiModels: List<PromoCheckoutVoucherOrdersItemUiModel> = listOf(),
    var cashbackAdvocateReferralAmount: Int = -1,
    var clashingInfoDetailUiModel: ClashingInfoDetailUiModel = ClashingInfoDetailUiModel(),
    var cashbackWalletAmount: Int = -1,
    var discountAmount: Int = -1,
    var titleDescription: String = "",
    var isTokopediaGerai: Boolean = false,
    var globalSuccess: Boolean = false,
    var trackingDetailUiModels: List<TrackingDetailsItemUiModel> = listOf(),
    var messageUiModel: MessageUiModel = MessageUiModel(),
    var gatewayId: String = "",
    var isCoupon: Int = -1,
    var couponDescription: String = "",
    var benefitDetailUiModels: List<BenefitDetailsItemUiModel> = listOf(),
    var additionalInfoUiModel: AdditionalInfoUiModel = AdditionalInfoUiModel(),
    var success: Boolean = false,
    var invoiceDescription: String = "",
    var benefitSummaryInfoUiModel: BenefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(),
    var tickerInfoUiModel: TickerInfoUiModel = TickerInfoUiModel(),
    var cashbackVoucherDescription: String = "",
    var tokopointsDetailUiModel: TokopointsDetailUiModel = TokopointsDetailUiModel()
) : Parcelable
