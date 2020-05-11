package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromoUiModel(
        var codes: List<String> = listOf(),
        var promoCodeId: Int = -1,
        var voucherOrderUiModels: List<PromoCheckoutVoucherOrdersItemUiModel?> = listOf(),
        var cashbackAdvocateReferralAmount: Int = -1,
        var clashingInfoDetailUiModel: ClashingInfoDetailUiModel = ClashingInfoDetailUiModel(),
        var cashbackWalletAmount: Int = -1,
        var discountAmount: Int = -1,
        var titleDescription: String = "",
        var isTokopediaGerai: Boolean = false,
        var globalSuccess: Boolean = false,
        var trackingDetails: List<TrackingDetailsItem?> = listOf(),
        var messageUiModel: MessageUiModel = MessageUiModel(),
        var gatewayId: String = "",
        var isCoupon: Int = -1,
        var couponDescription: String = "",
        var benefitDetailUiModels: List<BenefitDetailsItemUiModel?> = listOf(),
        var additionalInfoUiModel: AdditionalInfoUiModel = AdditionalInfoUiModel(),
        var success: Boolean = false,
        var invoiceDescription: String = "",
        var benefitSummaryInfoUiModel: BenefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(),
        var tickerInfoUiModel: TickerInfoUiModel = TickerInfoUiModel(),
        var cashbackVoucherDescription: String = "",
        var tokopointsDetailUiModel: TokopointsDetailUiModel = TokopointsDetailUiModel()
) : Parcelable
