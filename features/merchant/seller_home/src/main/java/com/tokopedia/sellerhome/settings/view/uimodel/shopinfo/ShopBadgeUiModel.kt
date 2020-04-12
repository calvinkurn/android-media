package com.tokopedia.sellerhome.settings.view.uimodel.shopinfo

import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable

data class ShopBadgeUiModel(val shopBadgeUrl: String? = "",
                            override val impressionEventName: String = "",
                            override val impressionEventCategory: String = "",
                            override val impressionEventAction: String = "",
                            override val clickEventAction: String = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_BADGE}") :
        SettingShopInfoImpressionTrackable,
        SettingShopInfoClickTrackable
