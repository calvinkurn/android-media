package com.tokopedia.gm.common.presentation.model


data class ShopInfoPeriodUiModel(
    var isNewSeller: Boolean = false,
    var periodType: String = "",
    var shopAge: Long = 0,
    var dateShopCreated: String = "",
    var periodStartDate: String = "",
    var periodEndDate: String = ""
)