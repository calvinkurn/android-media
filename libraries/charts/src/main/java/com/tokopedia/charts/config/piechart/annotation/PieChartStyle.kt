package com.tokopedia.charts.config.piechart.annotation

import androidx.annotation.IntDef

/**
 * Created By @ilhamsuaib on 07/07/20
 */

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(value = [PieChartStyle.DEFAULT, PieChartStyle.DONATE, PieChartStyle.DONATE_CURVE])
annotation class PieChartStyle {

    companion object {
        const val DEFAULT = 0
        const val DONATE = DEFAULT.plus(1)
        const val DONATE_CURVE = DONATE.plus(1)
    }
}