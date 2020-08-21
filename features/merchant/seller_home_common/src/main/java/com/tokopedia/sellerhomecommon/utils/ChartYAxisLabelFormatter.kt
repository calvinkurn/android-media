package com.tokopedia.sellerhomecommon.utils

import com.tokopedia.charts.common.utils.LabelFormatter
import com.tokopedia.charts.model.AxisLabel

/**
 * Created By @ilhamsuaib on 13/08/20
 */

class ChartYAxisLabelFormatter(
        private val labels: List<AxisLabel>
) : LabelFormatter {

    private val cache: MutableMap<Float, String> = mutableMapOf()
    private var i = 0

    override fun getAxisLabel(value: Float): String {
        var label: String = value.toInt().toString()
        if (!cache.containsKey(value)) {
            try {
                label = labels[i].valueFmt
                cache[value] = label
                if (i < labels.size) {
                    i++
                }
            } catch (e: IndexOutOfBoundsException) {
                if (!cache.containsValue(label)) {
                    i = 0
                    label = labels[i].valueFmt
                    cache[value] = label
                    i++
                }
            }
        } else {
            cache[value]?.let {
                label = it
            }
        }
        return label
    }
}