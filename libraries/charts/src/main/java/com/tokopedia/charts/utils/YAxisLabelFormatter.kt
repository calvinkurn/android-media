package com.tokopedia.charts.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.tokopedia.charts.model.YAxisLabel

/**
 * Created By @ilhamsuaib on 24/06/20
 */

class YAxisLabelFormatter(private val labels: List<YAxisLabel>) : ValueFormatter() {

    private val cache: MutableMap<Float, String> = mutableMapOf()
    private var i = 0

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        var label: String = value.toInt().toString()
        if (!cache.containsKey(value)) {
            try {
                label = labels[i].yLabel
                cache[value] = label
                if (i < labels.size) {
                    i++
                }
            } catch (e: IndexOutOfBoundsException) {
                i = 0
                label = labels[i].yLabel
                cache[value] = label
                i++
            }
        } else {
            cache[value]?.let {
                label = it
            }
        }
        return label
    }
}