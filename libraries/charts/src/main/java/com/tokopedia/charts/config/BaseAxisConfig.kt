package com.tokopedia.charts.config

import android.graphics.Color
import android.graphics.Typeface
import com.tokopedia.charts.config.annotation.ChartConfigDsl

/**
 * Created By @ilhamsuaib on 15/07/20
 */

@ChartConfigDsl
open class BaseAxisConfig {

    protected var typeface: Typeface? = null
    protected var isEnabled: Boolean = true
    protected var isLabelEnabled: Boolean = true
    protected var isGridEnabled: Boolean = true
    protected var textSize: Float = 12f
    protected var textColor: Int = Color.BLACK
    open protected var labelPosition: Int = 0

    open fun typeface(lambda: () -> Typeface) {
        typeface = lambda()
    }

    open fun enabled(lambda: () -> Boolean) {
        isEnabled = lambda()
    }

    open fun labelEnabled(lambda: () -> Boolean) {
        isLabelEnabled = lambda()
    }

    open fun gridEnabled(lambda: () -> Boolean) {
        isGridEnabled = lambda()
    }

    open fun labelPosition(lambda: () -> Int) {
        labelPosition = lambda()
    }

    open fun textSize(lambda: () -> Float) {
        textSize = lambda()
    }

    open fun textColor(lambda: () -> Int) {
        textColor = lambda()
    }
}