package com.tokopedia.sellerhome.settings.view.uimodel.menusetting

import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable

class OtherSettingsUiModel (buttonName: String,
                            override val clickEventName: String = SettingTrackingConstant.CLICK_SHOP_SETTING,
                            override val clickEventCategory: String = SettingTrackingConstant.SETTINGS,
                            override val clickEventAction: String = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.OTHER_SETTING} - $buttonName",
                            override val impressionEventName: String = SettingTrackingConstant.VIEW_SHOP_SETTING_IRIS,
                            override val impressionEventCategory: String = SettingTrackingConstant.SETTINGS,
                            override val impressionEventAction: String = "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.APPLICATION_SETTING} - $buttonName")
    : SettingShopInfoClickTrackable, SettingShopInfoImpressionTrackable {
}