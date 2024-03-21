package com.tokopedia.shopdiscount.subsidy.model.uimodel

data class ShopDiscountOptOutReasonUiModel(
    var reason: String = "",
    val isReasonFromResponse: Boolean = false,
    var isSelected: Boolean = false,
    var isInputError: Boolean = false
)
