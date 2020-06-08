package com.tokopedia.sellerhomecommon.presentation.view.linegraphconfig

import android.graphics.Color
import com.db.williamchart.util.DataSetConfiguration

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class LineGraphDataSetConfig : DataSetConfiguration {

    companion object {
        private const val COLOR_LINE = "#03ac0e"
        private const val COLOR_DOT = "#4fd15a"
    }

    override fun lineColor(): Int {
        return Color.parseColor(COLOR_LINE)
    }

    override fun pointColor(): Int {
        return Color.parseColor(COLOR_DOT)
    }

    override fun lineThickness(): Float {
        return 3f
    }

    override fun pointsSize(): Float {
        return 4f
    }

    override fun isVisible(): Boolean {
        return true
    }
}