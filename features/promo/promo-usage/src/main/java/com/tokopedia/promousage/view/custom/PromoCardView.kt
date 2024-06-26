package com.tokopedia.promousage.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PromoCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    companion object {
        private const val CARD_VIEW_CORNER_RADIUS = 24f // Corner radius for the CardView
        private const val EXPIRED_DATE_BACKGROUND_HEIGHT = 42f
        private const val CIRCLE_CUT_OUT_MARGIN_BOTTOM = 30f
        private const val CIRCLE_RADIUS = 20F
        private const val STROKE_WIDTH = 4f
    }

    init {
        radius = CARD_VIEW_CORNER_RADIUS
        cardElevation = 0f

        // Set card background to transparent to remove the default card view background
        setCardBackgroundColor(Color.TRANSPARENT)
    }

    private val circleCutStrokeColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200)
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }

    private val voucherBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN100)
        style = Paint.Style.FILL
    }

    private val voucherExpiredDateBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN50)
        style = Paint.Style.FILL
    }

    private val cardViewBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200)
        strokeWidth = STROKE_WIDTH
        style = Paint.Style.STROKE
    }

    private val cardViewCorners = floatArrayOf(
        CARD_VIEW_CORNER_RADIUS, CARD_VIEW_CORNER_RADIUS,   // Top left radius in px
        CARD_VIEW_CORNER_RADIUS, CARD_VIEW_CORNER_RADIUS,   // Top right radius in px
        CARD_VIEW_CORNER_RADIUS, CARD_VIEW_CORNER_RADIUS,   // Bottom right radius in px
        CARD_VIEW_CORNER_RADIUS, CARD_VIEW_CORNER_RADIUS    // Bottom left radius in px
    )

    private val voucherExpiryDateHeightPx by lazy { EXPIRED_DATE_BACKGROUND_HEIGHT.toPx() }
    private val voucherCircleMarginBottomPX by lazy { CIRCLE_CUT_OUT_MARGIN_BOTTOM.toPx() }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawLeftCircleCut(canvas)
        drawRightCircleCut(canvas)

        drawBackground(canvas)
        drawExpiredDateBackground(canvas)
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

    private fun drawExpiredDateBackground(canvas: Canvas?) {
        val top =
            (height - voucherExpiryDateHeightPx) + (voucherCircleMarginBottomPX / 2) - STROKE_WIDTH.toPx()
        val right = width.toFloat()
        val bottom = height.toFloat()
        canvas?.drawRoundRect(0f, top, right, bottom, 16f, 0f, voucherExpiredDateBackground)
    }

    fun updateToSelectedState() {
        circleCutStrokeColor.color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
        voucherBackground.color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN50)
        voucherExpiredDateBackground.color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN100)
        cardViewBorder.color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
        invalidate()
    }

    fun updateToNormalState() {
        circleCutStrokeColor.color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200)
        voucherBackground.color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
        voucherExpiredDateBackground.color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN50)
        cardViewBorder.color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN200)
        invalidate()
    }
}
