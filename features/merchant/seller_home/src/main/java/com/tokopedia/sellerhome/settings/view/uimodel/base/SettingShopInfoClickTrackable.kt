package com.tokopedia.sellerhome.settings.view.uimodel.base

import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant.CLICK_NAVIGATION_DRAWER
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant.OTHERS_TAB

interface SettingShopInfoClickTrackable {
    val clickEventName: String
        get() = CLICK_NAVIGATION_DRAWER
    val clickEventCategory: String
        get() = OTHERS_TAB
    val clickEventAction: String
    val clickEventLabel: String
        get() = ""
    val clickEventUserId: String
        get() = ""
    val clickEventShopId: String
        get() = ""
    val clickEventShopType: String
        get() = ""
}