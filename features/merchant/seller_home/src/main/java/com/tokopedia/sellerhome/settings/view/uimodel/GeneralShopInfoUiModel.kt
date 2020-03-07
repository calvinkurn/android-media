package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType

class GeneralShopInfoUiModel(val shopName: String = "",
                             val shopAvatarUrl: String = "",
                             val shopType: ShopType = RegularMerchant.NeedUpdate,
                             val saldoBalance: String = "",
                             val kreditTopAdsBalance: String = "")