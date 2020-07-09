package com.tokopedia.charts.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.charts.R
import com.tokopedia.charts.config.barchart.model.BarChartConfig
import kotlinx.android.synthetic.main.view_bar_chart.view.*

/**
 * Created By @ilhamsuaib on 09/07/20
 */

class BarChartView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_bar_chart, this)
    }

    fun init(config: BarChartConfig) {
        with(barChart) {

        }
    }
}