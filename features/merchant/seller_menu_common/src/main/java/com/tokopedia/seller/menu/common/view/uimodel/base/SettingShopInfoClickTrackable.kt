package com.tokopedia.seller.menu.common.view.uimodel.base

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant.CLICK_NAVIGATION_DRAWER
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant.OTHERS_TAB

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