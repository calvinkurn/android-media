package com.tokopedia.charts.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.utils.ViewPortHandler
import com.tokopedia.charts.common.utils.RoundedBarChartRenderer

class StackedBarDottedRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    radius: Int,
    private val barWithDottedLineIndex: Int,
    private val dottedLineColorString: String
) : RoundedBarChartRenderer(chart, animator, viewPortHandler, radius) {

    override fun drawValues(c: Canvas?) {
        super.drawValues(c)
        mBarBuffers?.getOrNull(barWithDottedLineIndex)?.let {
            c?.drawDottedLine(it)
        }
    }

    private fun getDottedLinePaint(): Paint {
        return Paint().apply {
            color = Color.parseColor(dottedLineColorString)
            style = Paint.Style.STROKE
            strokeWidth = 10f
            pathEffect = DashPathEffect(
                floatArrayOf(16f, 16f),
                0f
            )
        }
    }

    private fun Canvas.drawDottedLine(barBuffer: BarBuffer) {
        for (index in 0 until (barBuffer.buffer.size * mAnimator.phaseX).toInt() step 4) {
            val left = barBuffer.buffer[index] - 12f
            val right = barBuffer.buffer[index + 2] + 12f
            val top = barBuffer.buffer[index + 1] + 5f

            if (index == barWithDottedLineIndex) {
                drawRect(left, top, right + 10f, top, getDottedLinePaint())
            }
        }
    }
}
