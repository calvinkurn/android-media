package com.dompetia.sellerhomecommon.presentation.view.linegraphconfig

import android.graphics.Color
import com.db.williamchart.util.DataSetConfiguration

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class LineGraphDataSetConfig : DataSetConfiguration {
    override fun lineColor(): Int {
        return Color.parseColor("#03ac0e")
    }

    override fun pointColor(): Int {
        return Color.parseColor("#4fd15a")
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