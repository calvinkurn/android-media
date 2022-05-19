package com.tokopedia.gm.common.presentation.model


data class ShopInfoPeriodUiModel(
    var isNewSeller: Boolean = false,
    var shopAge: Long = 0,
    var dateShopCreated: String = ""
)