package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingSuccess
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType

class SettingShopInfoUiModel(val shopName: String = "",
                             val shopAvatar: String = "",
                             val shopType: ShopType = RegularMerchant.NeedUpgrade,
                             val saldoBalance: String = "",
                             val kreditTopAdsBalance: String = "",
                             val shopBadges: String = "",
                             val shopFollowers: Int = 0): SettingSuccess()