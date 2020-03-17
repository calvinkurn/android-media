package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use

data class ValidateUsePromoRevampUiModel(
        var promoUiModel: PromoUiModel = PromoUiModel(),
        var code: String = "",
        var errorCode: String = "",
        var message: List<String> = listOf(),
        var status: String = ""
)
