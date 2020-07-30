package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable

interface SettingTrackingListener {
    fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable)
}