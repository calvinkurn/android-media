package com.tokopedia.seller.menu.common.view.uimodel

data class UserShopInfoUiModel(
        var isOnDate: Boolean = false,
        var onDate: String = "",
        var totalTransaction: Int = 0,
        var badge: String = "",
        var textColorPMPro: Int? = null,
        var bgColorPMPro: Int? = null
)