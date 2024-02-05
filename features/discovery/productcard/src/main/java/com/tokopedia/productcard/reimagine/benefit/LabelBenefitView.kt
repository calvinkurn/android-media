package com.tokopedia.productcard.reimagine.benefit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.ProductCardLabel
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.productcard.utils.safeParseColor
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class LabelBenefitView : FrameLayout {

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

    private var circleCutoutFillPaint: Paint? = null
    private var circleCutoutStrokePaint: Paint? = null
    private var circleCutoutYPos: Float = 0f
    private var circleCutoutRadius: Float = 0f

    private fun init(attrs: AttributeSet? = null) {
        text = Typography(context).apply { initText() }
        addView(text)

        initBackground()

        initCircleCutout()
    }

    private fun Typography.initText() {
        id = productcardR.id.productCardLabelBenefitText
        textSize = 9f
        setWeight(Typography.BOLD)
        gravity = Gravity.CENTER
    }

    private fun initBackground() {
        background?.run {
            mutate()
            (this as? GradientDrawable)?.cornerRadius = 2.toPx().toFloat()
        }
    }

    private fun initCircleCutout() {
        circleCutoutFillPaint = Paint(ANTI_ALIAS_FLAG).apply { initCircleCutoutFillPaint() }
        circleCutoutStrokePaint = Paint(ANTI_ALIAS_FLAG).apply { initCircleCutoutStrokePaint() }
        circleCutoutYPos = getCircleCutoutYPos()
        circleCutoutRadius = 3.toPx().toFloat()
    }

    private fun Paint.initCircleCutoutFillPaint() {
        color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
        style = Paint.Style.FILL
        isDither = true
    }

    private fun Paint.initCircleCutoutStrokePaint() {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeWidth = 1.toPx().toFloat()
    }

    private fun getCircleCutoutYPos(): Float =
        context.getPixel(productcardR.dimen.product_card_reimagine_label_benefit_height) / 2f

    fun render(labelGroup: ProductCardModel.LabelGroup?) {
        if (labelGroup == null) {
            hide()
        } else {
            showLabelBenefit(labelGroup)
        }
    }

    private fun showLabelBenefit(labelGroup: ProductCardModel.LabelGroup) {
        ProductCardLabel(background, text).render(labelGroup)

        val outlineColor = labelGroup.outlineColor() ?: ""
        circleCutoutStrokePaint?.color = safeParseColor(outlineColor, Color.TRANSPARENT)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return
        val circleCutoutFillPaint = circleCutoutFillPaint ?: return
        val circleCutoutStrokePaint = circleCutoutStrokePaint ?: return

        canvas.drawCircle(0f, circleCutoutYPos, circleCutoutRadius, circleCutoutFillPaint)
        canvas.drawCircle(width.toFloat(), circleCutoutYPos, circleCutoutRadius, circleCutoutFillPaint)

        canvas.drawCircle(0f, circleCutoutYPos, circleCutoutRadius, circleCutoutStrokePaint)
        canvas.drawCircle(width.toFloat(), circleCutoutYPos, circleCutoutRadius, circleCutoutStrokePaint)
    }

    fun forceLightMode() {
        circleCutoutFillPaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.dms_static_light_NN100)
            style = Paint.Style.FILL
            isDither = true
        }

        val outlineColor = circleCutoutStrokePaint?.color ?: return

        circleCutoutStrokePaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = outlineColor
            style = Paint.Style.STROKE
            strokeWidth = 1.toPx().toFloat()
        }

        invalidate()
    }
}
