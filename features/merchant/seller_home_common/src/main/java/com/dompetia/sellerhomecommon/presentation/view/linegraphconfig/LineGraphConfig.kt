package com.dompetia.sellerhomecommon.presentation.view.linegraphconfig

import android.graphics.Color
import com.db.williamchart.Tools
import com.db.williamchart.renderer.AxisRenderer.LabelPosition
import com.db.williamchart.renderer.StringFormatRenderer
import com.db.williamchart.util.AnimationGraphConfiguration
import com.db.williamchart.util.KMNumbers
import com.db.williamchart.view.ChartView

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class LineGraphConfig : AnimationGraphConfiguration {

    private var marginTop = 0
    private var marginRight = 0
    private var marginBottom = 0

    override fun labelColor(): Int {
        return Color.parseColor("#ae31353b")
    }

    override fun axisColor(): Int {
        return Color.argb(13, 0, 0, 0)
    }

    override fun gridThickness(): Float {
        return 1f
    }

    override fun gridColor(): Int {
        return Color.argb(13, 0, 0, 0)
    }

    override fun xLabelPosition(): LabelPosition {
        return LabelPosition.OUTSIDE
    }

    override fun yLabelPosition(): LabelPosition {
        return LabelPosition.OUTSIDE
    }

    override fun gridType(): ChartView.GridType {
        return ChartView.GridType.NONE
    }

    override fun xAxis(): Boolean {
        return true
    }

    override fun yAxis(): Boolean {
        return true
    }

    override fun xDataGrid(): Boolean {
        return true
    }

    override fun yStringFormatRenderer(): StringFormatRenderer {
        return StringFormatRenderer { rawString: String? -> KMNumbers.formatSuffixNumbers(java.lang.Long.valueOf(rawString)) }
    }

    override fun topMargin(): Int {
        return marginTop
    }

    override fun rightMargin(): Int {
        return marginRight
    }

    override fun bottomMargin(): Int {
        return marginBottom
    }

    override fun xDistAxisToLabel(): Float {
        return Tools.fromDpToPx(10f)
    }

    override fun yDistAxisToLabel(): Float {
        return Tools.fromDpToPx(10f)
    }

    override fun alpha(): Int {
        return 1
    }

    override fun duration(): Int {
        return 500
    }

    override fun easingId(): Int {
        return 0
    }

    override fun overlapFactor(): Float {
        return 1f
    }

    override fun startX(): Float {
        return 0f
    }

    override fun startY(): Float {
        return 1f
    }

    override fun endAnimation(): Runnable {
        return Runnable {}
    }

    fun setMarginTop(marginTop: Int) {
        this.marginTop = marginTop
    }

    fun setMarginRight(marginRight: Int) {
        this.marginRight = marginRight
    }

    fun setMarginBottom(marginBottom: Int) {
        this.marginBottom = marginBottom
    }
}