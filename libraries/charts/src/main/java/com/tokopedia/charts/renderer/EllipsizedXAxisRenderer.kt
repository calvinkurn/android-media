package com.tokopedia.charts.renderer

import android.graphics.Canvas
import android.text.TextPaint
import android.text.TextUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.abs

class EllipsizedXAxisRenderer(
        viewPortHandler: ViewPortHandler,
        xAxis: XAxis,
        trans: Transformer
) : com.github.mikephil.charting.renderer.XAxisRenderer(viewPortHandler, xAxis, trans) {

    companion object {
        private const val NO_POSITION = -1f
        private const val MAX_TEXT_WIDTH_PERCENTAGE = 0.80f
    }

    private fun calculateXAxis(): FloatArray {
        val centeringEnabled = mXAxis.isCenterAxisLabelsEnabled

        val positions = FloatArray(mXAxis.mEntryCount * 2)

        for (i in positions.indices step 2) {
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2]
            } else {
                positions[i] = mXAxis.mEntries[i / 2]
            }
        }

        mTrans.pointValuesToPixel(positions)

        for (i in positions.indices step 2) {
            if (mViewPortHandler.isInBoundsX(positions[i])) {
                val label = mXAxis.valueFormatter.getAxisLabel(mXAxis.mEntries[i / 2], mXAxis)
                if (mXAxis.isAvoidFirstLastClippingEnabled) {
                    if (i / 2 == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                        val width = Utils.calcTextWidth(mAxisLabelPaint, label)
                        if (width > mViewPortHandler.offsetRight() * 2 && positions[i] + width > mViewPortHandler.chartWidth) {
                            positions[i] = positions[i] - width / 2
                        }
                    } else if (i == 0) {
                        val width = Utils.calcTextWidth(mAxisLabelPaint, label)
                        positions[i] = positions[i] + width / 2
                    }
                }
            } else {
                positions[i] = NO_POSITION
                positions[i + 1] = NO_POSITION
            }
        }

        return positions
    }

    private fun drawLabel(c: Canvas?, formattedLabel: String?, x: Float, y: Float, maxX: Float, anchor: MPPointF?, angleDegrees: Float) {
        if (maxX != NO_POSITION) {
            val trimmedLabel = TextUtils.ellipsize(formattedLabel, TextPaint(mAxisLabelPaint), abs(maxX - x) * MAX_TEXT_WIDTH_PERCENTAGE, TextUtils.TruncateAt.END)
            drawLabel(c, trimmedLabel.toString(), x, y, anchor, angleDegrees)
        } else {
            drawLabel(c, formattedLabel, x, y, anchor, angleDegrees)
        }
    }

    override fun drawLabels(c: Canvas?, pos: Float, anchor: MPPointF?) {
        val labelRotationAngleDegrees = mXAxis.labelRotationAngle
        val positions = calculateXAxis()
        for (i in positions.indices step 2) {
            if (positions[i] != NO_POSITION) {
                val label = mXAxis.valueFormatter.getAxisLabel(mXAxis.mEntries[i / 2], mXAxis)
                val maxX = positions.getOrElse(i + 2) { NO_POSITION }
                drawLabel(c, label, positions[i], pos, maxX, anchor, labelRotationAngleDegrees)
            }
        }
    }
}