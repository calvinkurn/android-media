package com.tokopedia.seller.menu.common.view.uimodel.shopinfo

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.uimodel.base.BalanceType

class TopadsBalanceUiModel(
    topAdsBalanceValue: String,
    override val impressionEventAction: String = "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.ON_TOPADS_CREDIT}"
) : BalanceUiModel(BalanceType.TOPADS, topAdsBalanceValue)