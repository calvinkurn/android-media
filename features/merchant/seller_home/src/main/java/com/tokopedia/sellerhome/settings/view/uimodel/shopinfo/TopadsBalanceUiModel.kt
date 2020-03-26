package com.tokopedia.sellerhome.settings.view.uimodel.shopinfo

import com.tokopedia.sellerhome.settings.view.uimodel.base.BalanceType

class TopadsBalanceUiModel(val isTopAdsUser: Boolean,
                           topAdsBalanceValue: String) : BalanceUiModel(BalanceType.TOPADS, topAdsBalanceValue)