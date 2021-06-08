package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.gm.common.constant.PeriodType

/**
 * Created By @ilhamsuaib on 25/03/21
 */

data class PowerMerchantBasicInfoUiModel(
        val pmStatus: PMStatusUiModel,
        val shopInfo: PMShopInfoUiModel,
        val tickers: List<TickerUiModel> = emptyList(),
        val periodTypePmPro: String = PeriodType.COMMUNICATION_PERIOD_PM_PRO,
        val isFreeShippingEnabled: Boolean = false
)