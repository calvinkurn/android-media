package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromoUiModel(
        var codes: List<String> = listOf(),
        var voucherOrderUiModels: List<PromoCheckoutVoucherOrdersItemUiModel?> = listOf(),
        var clashingInfoDetailUiModel: ClashingInfoDetailUiModel = ClashingInfoDetailUiModel(),
        var titleDescription: String = "",
        var globalSuccess: Boolean = false,
        var trackingDetailUiModels: List<TrackingDetailsItemUiModel?> = listOf(),
        var messageUiModel: MessageUiModel = MessageUiModel(),
        var additionalInfoUiModel: AdditionalInfoUiModel = AdditionalInfoUiModel(),
        var success: Boolean = false,
        var benefitSummaryInfoUiModel: BenefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(),
        var tickerInfoUiModel: TickerInfoUiModel = TickerInfoUiModel(),
        var tokopointsDetailUiModel: TokopointsDetailUiModel = TokopointsDetailUiModel()
) : Parcelable
