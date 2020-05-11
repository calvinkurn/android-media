package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BenefitDetailsItemUiModel(
		var code: String = "",
		var uniqueId: String = "",
		var cashbackAmount: Int = -1,
		var promoTypeUiModel: PromoTypeUiModel = PromoTypeUiModel(),
		var discountAmount: Int = -1,
		var cashbackDetails: List<PromoCashbackDetailsUiModel> = listOf(),
		var discountDetailUiModels: List<DiscountDetailsItemUiModel> = listOf(),
		var benefitProductDetails: List<BenefitProductDetailsItemUiModel> = listOf(),
		var type: String = "",
		var orderId: Int = -1
): Parcelable
