package com.tokopedia.sellerhome.settings.view.uimodel.shopinfo

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable

data class ShopAvatarUiModel(val shopAvatarUrl: String,
                             override val impressionEventAction: String = "",
                             override val impressionEventLabel: String = "",
                             override val clickEventAction: String = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_PICTURE}",
                             override val clickEventLabel: String = "",
                             override val impressHolder: ImpressHolder = ImpressHolder()) :
        SettingShopInfoImpressionTrackable,
        SettingShopInfoClickTrackable