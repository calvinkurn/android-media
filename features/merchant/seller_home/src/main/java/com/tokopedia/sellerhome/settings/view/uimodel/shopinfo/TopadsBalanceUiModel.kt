package com.tokopedia.sellerhome.settings.view.uimodel.shopinfo

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.view.uimodel.base.BalanceType

class TopadsBalanceUiModel(val isTopAdsUser: Boolean,
                           topAdsBalanceValue: String,
                           override val impressionEventAction: String = "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.ON_TOPADS_CREDIT}")
    : BalanceUiModel(BalanceType.TOPADS, topAdsBalanceValue)