package com.tokopedia.sellerhome.settings.view.uimodel.menusetting

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable

class OtherSettingsUiModel (buttonName: String,
                            trackingAlias: String? = null,
                            override val clickEventName: String = SettingTrackingConstant.CLICK_SHOP_SETTING,
                            override val clickEventCategory: String = SettingTrackingConstant.SETTINGS,
                            override val clickEventAction: String = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.OTHER_SETTING} - $buttonName",
                            override val impressionEventName: String = SettingTrackingConstant.VIEW_SHOP_SETTING_IRIS,
                            override val impressionEventCategory: String = SettingTrackingConstant.SETTINGS,
                            override val impressionEventAction: String = "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.APPLICATION_SETTING} - $trackingAlias")
    : SettingShopInfoClickTrackable, SettingShopInfoImpressionTrackable