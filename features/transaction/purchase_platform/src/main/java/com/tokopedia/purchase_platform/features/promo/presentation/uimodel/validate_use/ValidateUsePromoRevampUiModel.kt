package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use

import android.os.Parcel
import android.os.Parcelable


data class ValidateUsePromoRevampUiModel(
        var promoUiModel: PromoUiModel? = PromoUiModel(),
        var code: String? = "",
        var errorCode: String? = "",
        var message: List<Any?>? = listOf(),
        var status: String? = ""
)
