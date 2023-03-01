package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
) : Parcelable
