package com.tokopedia.seller.menu.common.view.uimodel.base

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant.OTHERS_TAB
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant.VIEW_NAVIGATION_IRIS

interface SettingShopInfoImpressionTrackable {
    val impressHolder: ImpressHolder
        get() = ImpressHolder()
    val impressionEventName: String
        get() = VIEW_NAVIGATION_IRIS
    val impressionEventCategory: String
        get() = OTHERS_TAB
    val impressionEventAction: String
    val impressionEventLabel: String
        get() = ""
}