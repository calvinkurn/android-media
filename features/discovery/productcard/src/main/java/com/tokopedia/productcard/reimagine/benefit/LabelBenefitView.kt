package com.tokopedia.productcard.reimagine.benefit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Region
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.productcard.reimagine.ProductCardLabel
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.productcard.R as productcardR

class LabelBenefitView: FrameLayout {

    var text: Typography? = null
        private set

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        text = Typography(context).also {
            it.id = productcardR.id.productCardLabelBenefitText
            it.textSize = 9f
            it.setWeight(Typography.BOLD)
            it.gravity = Gravity.CENTER
        }

        addView(text)

        background?.run {
            mutate()
            (this as? GradientDrawable)?.cornerRadius = 2.toPx().toFloat()
        }
    }

    fun render(labelGroup: ProductCardModel.LabelGroup?) {
        if (labelGroup == null) hide()
        else showLabelBenefit(labelGroup)
    }

    private fun showLabelBenefit(labelGroup: ProductCardModel.LabelGroup) {
        ProductCardLabel(background, text).render(labelGroup)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawLeftCircleCut(canvas)
    }

    private fun drawLeftCircleCut(canvas: Canvas?) {
        // Left cut stroke color
//        val circlePath = Path().also {
//            it.addCircle(
//                0f,
//                9.toPx().toFloat(),
//                3.toPx().toFloat(),
//                Path.Direction.CW
//            )
//        }
//        canvas?.drawPath(circlePath, Paint(Paint.ANTI_ALIAS_FLAG).apply {
//            color = Color.parseColor("#FFB2C2")
//            style = Paint.Style.STROKE
//            strokeWidth = 1.toPx().toFloat()
//        })
//
//        // Left cut circle
//        canvas?.clipPath(Path().also {
//            it.addRect(
//                3.toPx().toFloat(),
//                9.toPx().toFloat(),
//                3.toPx().toFloat(),
//                9.toPx().toFloat(),
//                Path.Direction.CW
//            )
//        }, Region.Op.DIFFERENCE)
    }

    private fun drawRightCircleCut(canvas: Canvas?) {
//        // Right cut stroke color
//        canvas?.drawPath(Path().also {
//            it.addCircle(
//                width.toFloat(),
//                (height - voucherCircleMarginBottomPX),
//                CIRCLE_RADIUS,
//                Path.Direction.CW
//            )
//        }, circleCutStrokeColor)
//
//        // Right cut circle
//        canvas?.clipPath(Path().also {
//            it.addCircle(
//                width.toFloat(),
//                (height - voucherCircleMarginBottomPX),
//                CIRCLE_RADIUS,
//                Path.Direction.CW
//            )
//        }, Region.Op.DIFFERENCE)
    }
}
