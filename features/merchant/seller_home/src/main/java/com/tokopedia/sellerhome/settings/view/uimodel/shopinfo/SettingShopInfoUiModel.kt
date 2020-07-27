package com.tokopedia.sellerhome.settings.view.uimodel.shopinfo

import com.tokopedia.sellerhome.settings.view.uimodel.base.BalanceType
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingSuccess
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import com.tokopedia.user.session.UserSessionInterface

class SettingShopInfoUiModel(val shopName: String = "",
                             private val shopAvatar: String = "",
                             private val shopType: ShopType = RegularMerchant.NeedUpgrade,
                             private val saldoBalance: String = "",
                             private val kreditTopAdsBalance: String = "",
                             private val isTopadsUser: Boolean = false,
                             private val shopBadges: String = "",
                             private val shopFollowers: Int = 0,
                             private val user: UserSessionInterface? = null): SettingSuccess() {

    val shopAvatarUiModel by lazy { ShopAvatarUiModel(shopAvatar) }
    val shopBadgeUiModel by lazy { ShopBadgeUiModel(shopBadges) }
    val shopFollowersUiModel by lazy { ShopFollowersUiModel(shopFollowers) }
    val shopStatusUiModel by lazy { ShopStatusUiModel(shopType, user) }
    val saldoBalanceUiModel by lazy { BalanceUiModel(BalanceType.SALDO, saldoBalance) }
    val topadsBalanceUiModel by lazy { TopadsBalanceUiModel(isTopadsUser, kreditTopAdsBalance) }
}