package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataUiModel(
		var globalSuccess: Boolean = false,
		var success: Boolean = false,
		var message: MessageUiModel = MessageUiModel(),
		var promoCodeId: String = "-1",
		var codes: List<String> = emptyList(),
		var titleDescription: String = "",
		var discountAmount: Long = -1L,
		var cashbackWalletAmount: Long = -1L,
		var cashbackAdvocateReferralAmount: Long = -1L,
		var cashbackVoucherDescription: String = "",
		var invoiceDescription: String = "",
		var gatewayId: String = "",
		var isCoupon: Int = -1,
		var couponDescription: String = "",
		var benefit: BenefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(),
		var clashings: ClashingInfoDetailUiModel = ClashingInfoDetailUiModel(),
		var voucherOrders: List<VoucherOrdersItemUiModel> = emptyList(),
		var trackingDetailUiModel: List<TrackingDetailUiModel> = emptyList(),
		var tickerInfoUiModel: TickerInfoUiModel = TickerInfoUiModel()
) : Parcelable