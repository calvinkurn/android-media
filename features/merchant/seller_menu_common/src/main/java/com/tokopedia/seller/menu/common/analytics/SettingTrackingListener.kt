package com.tokopedia.seller.menu.common.analytics

import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable

interface SettingTrackingListener {
    fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable)
}