package com.tokopedia.discovery_component.widgets.automatecoupon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CouponCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attributeSet, defStyleAttr) {

    companion object {
        private const val CARD_VIEW_CORNER_RADIUS = 24f
        private const val CIRCLE_CUT_OUT_MARGIN_BOTTOM = 42f
        private const val CIRCLE_RADIUS = 20F
        private const val STROKE_WIDTH = 4f
    }

    init {
        radius = CARD_VIEW_CORNER_RADIUS
        cardElevation = 0f

        setCardBackgroundColor(Color.TRANSPARENT)
    }

    private val circleCutStrokeColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200)
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }

    private val voucherBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
        style = Paint.Style.FILL
    }

    private val dividerLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN300)
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 2f
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f, 10f, 10f), 0f)
    }

    private val cardViewBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200)
        strokeWidth = STROKE_WIDTH
        style = Paint.Style.STROKE
    }

    private val cardViewCorners = floatArrayOf(
        CARD_VIEW_CORNER_RADIUS, CARD_VIEW_CORNER_RADIUS,
        CARD_VIEW_CORNER_RADIUS, CARD_VIEW_CORNER_RADIUS,
        CARD_VIEW_CORNER_RADIUS, CARD_VIEW_CORNER_RADIUS,
        CARD_VIEW_CORNER_RADIUS, CARD_VIEW_CORNER_RADIUS
    )

    private val voucherCircleMarginBottomPX by lazy { CIRCLE_CUT_OUT_MARGIN_BOTTOM.toPx() }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawLeftCircleCut(canvas)
        drawRightCircleCut(canvas)

        drawBackground(canvas)
        drawDivider(canvas)
    }

    override fun dispatchDraw(canvas: Canvas) {
        drawCardViewBorder(canvas)
        super.dispatchDraw(canvas)
    }

    private fun drawLeftCircleCut(canvas: Canvas?) {
        // Left cut stroke color
        canvas?.drawPath(Path().also {
            it.addCircle(
                0f,
                (height - voucherCircleMarginBottomPX),
                CIRCLE_RADIUS,
                Path.Direction.CW
            )
        }, circleCutStrokeColor)

        // Left cut circle
        canvas?.clipPath(Path().also {
            it.addCircle(
                0f,
                (height - voucherCircleMarginBottomPX),
                CIRCLE_RADIUS,
                Path.Direction.CW
            )
        }, Region.Op.DIFFERENCE)
    }

    private fun drawRightCircleCut(canvas: Canvas?) {
        // Right cut stroke color
        canvas?.drawPath(Path().also {
            it.addCircle(
                width.toFloat(),
                (height - voucherCircleMarginBottomPX),
                CIRCLE_RADIUS,
                Path.Direction.CW
            )
        }, circleCutStrokeColor)

        // Right cut circle
        canvas?.clipPath(Path().also {
            it.addCircle(
                width.toFloat(),
                (height - voucherCircleMarginBottomPX),
                CIRCLE_RADIUS,
                Path.Direction.CW
            )
        }, Region.Op.DIFFERENCE)
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

    private fun drawBackground(canvas: Canvas?) {
        val right = width.toFloat()
        val bottom = height.toFloat()
        canvas?.drawRoundRect(0f, 0f, right, bottom, 16f, 0f, voucherBackground)
    }

    private fun drawDivider(canvas: Canvas?) {
        val yPosition = height - voucherCircleMarginBottomPX
        val marginHorizontal = 10f
        canvas?.drawLine(
            marginHorizontal,
            yPosition,
            (width.toFloat() - CIRCLE_RADIUS - marginHorizontal),
            yPosition,
            dividerLine
        )
    }
}
