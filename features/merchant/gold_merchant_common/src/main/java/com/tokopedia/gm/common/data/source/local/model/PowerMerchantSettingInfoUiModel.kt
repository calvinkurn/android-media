package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.gm.common.constant.PeriodType

/**
 * Created By @ilhamsuaib on 01/03/21
 */

data class PowerMerchantSettingInfoUiModel(
        val periodeType: String = PeriodType.COMMUNICATION_PERIOD,
        val tickers: List<TickerUiModel> = emptyList()
)