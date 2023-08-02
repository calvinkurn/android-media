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


class VoucherCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {


    init {
        cardElevation = 4f
    }
    private val borderWidth = 2f // Width of the border
    private val cornerRadius = 16f // Corner radius for the CardView

    private val borderPaint = Paint().apply {
        color = Color.GRAY // Set the desired border color
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
        isAntiAlias = true
    }
    private val bottomGrayBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
        style = Paint.Style.FILL
    }

    private val bottomGreenBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        style = Paint.Style.FILL
        strokeWidth = 4f
        isAntiAlias = true
    }

    private val cardViewBorderGray = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    private val cardViewBorderGreen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN100)
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val circleCutStrokeColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val corners = floatArrayOf(
        32f, 32f,   // Top left radius in px
        32f, 32f,   // Top right radius in px
        32f, 32f,     // Bottom right radius in px
        32f, 32f      // Bottom left radius in px
    )

    private val borderPath by lazy {
        Path().apply {
            addRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                corners,
                Path.Direction.CW
            )

            close()

            /*lineTo(width.toFloat(), 0f) //Top
            lineTo(width.toFloat(), (height - voucherExpiryDateHeightPx)) //Right (above circle)
            lineTo(width.toFloat(), height.toFloat()) // Right (below circle)
            lineTo(0f, height.toFloat()) //Bottom
            lineTo(0f, (height - voucherExpiryDateHeightPx)) //Left (below circle)
            lineTo(0f, 0f) //Left (above circle)
            close()*/
        }
    }
    private val voucherExpiryDateHeightPx by lazy { dpToPx(32f).toFloat() }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawLeftCircleCut(canvas)
        drawRightCircleCut(canvas)

        drawBottomBackground(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        drawCardViewBorder(canvas)

        super.dispatchDraw(canvas)

    }
    private fun drawLeftCircleCut(canvas: Canvas?) {
        //Left cut
        canvas?.clipPath(Path().also {
            it.addCircle(0f, (height - voucherExpiryDateHeightPx), 20f, Path.Direction.CW)
        }, Region.Op.DIFFERENCE)

        //Left cut stroke color
        canvas?.drawPath(Path().also {
            it.addCircle(0f, (height - voucherExpiryDateHeightPx), 20f, Path.Direction.CW)
        }, circleCutStrokeColor)

    }

    private fun drawRightCircleCut(canvas: Canvas?) {
        //Right cut
        canvas?.clipPath(Path().also {
            it.addCircle(
                width.toFloat(),
                (height - voucherExpiryDateHeightPx),
                20f,
                Path.Direction.CW
            )
        }, Region.Op.DIFFERENCE)

        //Right cut stroke color
        canvas?.drawPath(Path().also {
            it.addCircle(
                width.toFloat(),
                (height - voucherExpiryDateHeightPx),
                20f,
                Path.Direction.CW
            )
        }, circleCutStrokeColor)

    }

    private fun drawCardViewBorder(canvas: Canvas?) {
        canvas?.drawPath(borderPath, cardViewBorderGray)
    }

    private fun drawBottomBackground(canvas: Canvas?) {
        canvas?.drawRoundRect(
            0f,
            (height - voucherExpiryDateHeightPx),
            width.toFloat(),
            height.toFloat(),
            16f,
            0f,
            bottomGrayBackground
        )
    }

    private fun dpToPx(dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}
