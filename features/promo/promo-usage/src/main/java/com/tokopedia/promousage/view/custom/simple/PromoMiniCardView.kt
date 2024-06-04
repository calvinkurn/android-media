package com.tokopedia.promousage.view.custom.simple

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PromoMiniCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    init {
        radius = CARD_VIEW_CORNER_RADIUS
        cardElevation = 0f
    }

    private val cardViewCorners = floatArrayOf(
        CARD_VIEW_CORNER_RADIUS,
        CARD_VIEW_CORNER_RADIUS, // Top left radius in px
        CARD_VIEW_CORNER_RADIUS,
        CARD_VIEW_CORNER_RADIUS, // Top right radius in px
        CARD_VIEW_CORNER_RADIUS,
        CARD_VIEW_CORNER_RADIUS, // Bottom right radius in px CARD_VIEW_CORNER_RADIUS,
        CARD_VIEW_CORNER_RADIUS,
        CARD_VIEW_CORNER_RADIUS // Bottom left radius in px
    )

    private val circleFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200) // default color
        style = Paint.Style.FILL
    }

    private val circleCutStrokeColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200)
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }

    private val cardViewBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200)
        strokeWidth = STROKE_WIDTH
        style = Paint.Style.STROKE
    }

    override fun dispatchDraw(canvas: Canvas) {
        drawCardViewBorder(canvas)
        super.dispatchDraw(canvas)
    }

    private fun drawCardViewBorder(canvas: Canvas?) {
        val cardViewBorderPath = Path().apply {
            addRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                cardViewCorners,
                Path.Direction.CW
            )
            close()
        }
        canvas?.drawPath(cardViewBorderPath, cardViewBorder)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLeftCircleCut(canvas)
        drawRightCircleCut(canvas)
    }

    private fun drawLeftCircleCut(canvas: Canvas?) {
        // Left cut fill color
        canvas?.drawPath(
            Path().also {
                it.addCircle(
                    0f,
                    (height / 2).toFloat(),
                    CIRCLE_RADIUS,
                    Path.Direction.CW
                )
            },
            circleFillPaint
        )

        // Left cut circle
        canvas?.clipPath(
            Path().also {
                it.addCircle(
                    0f,
                    (height / 2).toFloat(),
                    CIRCLE_RADIUS,
                    Path.Direction.CW
                )
            },
            Region.Op.DIFFERENCE
        )

        // Left cut stroke color
        canvas?.drawPath(
            Path().also {
                it.addCircle(
                    0f,
                    (height / 2).toFloat(),
                    CIRCLE_RADIUS,
                    Path.Direction.CW
                )
            },
            circleCutStrokeColor
        )
    }

    private fun drawRightCircleCut(canvas: Canvas?) {
        // Right cut fill color
        canvas?.drawPath(
            Path().also {
                it.addCircle(
                    width.toFloat(),
                    (height / 2).toFloat(),
                    CIRCLE_RADIUS,
                    Path.Direction.CW
                )
            },
            circleFillPaint
        )

        // Right cut circle
        canvas?.clipPath(
            Path().also {
                it.addCircle(
                    width.toFloat(),
                    (height / 2).toFloat(),
                    CIRCLE_RADIUS,
                    Path.Direction.CW
                )
            },
            Region.Op.DIFFERENCE
        )

        // Right cut stroke color
        canvas?.drawPath(
            Path().also {
                it.addCircle(
                    width.toFloat(),
                    (height / 2).toFloat(),
                    CIRCLE_RADIUS,
                    Path.Direction.CW
                )
            },
            circleCutStrokeColor
        )
    }

    fun changeCircleCutColor(color: Int, alpha: Int) {
        this.circleFillPaint.color = ContextCompat.getColor(context, color)
        this.circleFillPaint.alpha = alpha
        invalidate() // Invalidate to request a redraw with the new color
    }

    companion object {
        private const val CARD_VIEW_CORNER_RADIUS = 24f // Corner radius for the CardView
        private const val CIRCLE_RADIUS = 20F
        private const val STROKE_WIDTH = 4f
    }
}
