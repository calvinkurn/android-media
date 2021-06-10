package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType

data class UserShopInfoWrapper(val shopType: ShopType?,
                               val userShopInfoUiModel: UserShopInfoUiModel? = UserShopInfoUiModel()) {
    data class UserShopInfoUiModel(
            var isBeforeOnDate: Boolean = false,
            var onDate: String = "",
            var totalTransaction: Int = 0,
            var badge: String = "",
            var shopTierName: String = "",
            var shopTier: Int = -1,
            var pmProGradeName: String = "",
            var periodTypePmPro: String = ""
    )
}