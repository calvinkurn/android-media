package com.tokopedia.sellerhome.settings.view.uimodel.shopinfo

import com.tokopedia.sellerhome.settings.view.uimodel.base.BalanceType
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingSuccess
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType

class SettingShopInfoUiModel(val shopName: String = "",
                             val shopAvatar: String = "",
                             val shopType: ShopType = RegularMerchant.NeedUpgrade,
                             val saldoBalance: String = "",
                             val kreditTopAdsBalance: String = "",
                             val shopBadges: String = "",
                             val shopFollowers: Int = 0): SettingSuccess() {

    val shopAvatarUiModel by lazy { ShopAvatarUiModel(shopAvatar) }
    val shopBadgeUiModel by lazy { ShopBadgeUiModel(shopBadges) }
    val shopFollowersUiModel by lazy { ShopFollowersUiModel(shopFollowers) }
    val shopStatusUiModel by lazy { ShopStatusUiModel(shopType) }
    val saldoBalanceUiModel by lazy { BalanceUiModel(BalanceType.SALDO, saldoBalance) }
    val topadsBalanceUiModel by lazy { BalanceUiModel(BalanceType.TOPADS, kreditTopAdsBalance) }
}