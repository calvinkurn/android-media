package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidateUsePromoRevampUiModel(
    var promoUiModel: PromoUiModel = PromoUiModel(),
    var code: String = "",
    var errorCode: String = "",
    var message: List<String> = listOf(),
    var status: String = ""
) : Parcelable
