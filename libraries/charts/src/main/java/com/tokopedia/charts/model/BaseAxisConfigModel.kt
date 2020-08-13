package com.tokopedia.charts.model

import android.graphics.Typeface

/**
 * Created By @ilhamsuaib on 15/07/20
 */

internal interface BaseAxisConfigModel {
    val typeface: Typeface?
    val isEnabled: Boolean
    val isLabelEnabled: Boolean
    val isGridEnabled: Boolean
    val mLabelPosition: Int
    val axisMinimum: Float
}