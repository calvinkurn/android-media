package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 10/07/20
 */

data class ChartSummaryUiModel(
        val diffPercentage: Int = 0,
        val diffPercentageFmt: String = "",
        val value: Int = 0,
        val valueFmt: String = ""
)