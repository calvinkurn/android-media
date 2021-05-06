package com.tokopedia.mvcwidget.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.mvcwidget.R
import com.tokopedia.promoui.common.dpToPx

open class BaseShadowLayout : ConstraintLayout {

    private var borderPaint = Paint()
    private var rectPaint = Paint()
    var shadowPaint = Paint()

    var shadowPath = Path()
    protected var clipPath = Path()
    private var rectBackgroundPath = Path()

    protected var clipRectF = RectF()
    private var rectBackgroundRectF = RectF()
    private var borderRectF = RectF()

    var porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)

    open var shadowColor = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G900)

    var shadowStrokeWidth = 15f

    open var blurRadius = 50f
    private var cornerRadius = 8f
    private var shadowHeight = 0f
    var shadowDx = 0f
    var shadowDy = 0f
    var shadowStartY = java.lang.Float.MIN_VALUE
    open var enableShadow = false
    lateinit var blurMaskFilter: BlurMaskFilter

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    private fun init(attrs: AttributeSet?) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        readAttrs(attrs)
        blurMaskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
    }

    open fun readAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray =
                    context.theme.obtainStyledAttributes(attrs, R.styleable.BaseShadowLayout, 0, 0)
            shadowHeight = typedArray.getDimension(R.styleable.BaseShadowLayout_shadowHeight, 0f)
            shadowDx =
                    typedArray.getDimension(R.styleable.BaseShadowLayout_shadowDx, 0f)
            shadowDy =
                    typedArray.getDimension(R.styleable.BaseShadowLayout_shadowDy, 0f)
            shadowStartY = typedArray.getDimension(
                    R.styleable.BaseShadowLayout_shadowStartY,
                    java.lang.Float.MIN_VALUE
            )
            shadowColor = typedArray.getColor(R.styleable.BaseShadowLayout_shadowColor, androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G900))
            shadowStrokeWidth =
                    typedArray.getDimension(
                            R.styleable.BaseShadowLayout_shadowStrokeWidth,
                            dpToPx(1)
                    )
            cornerRadius = typedArray.getDimension(
                    R.styleable.BaseShadowLayout_cornerRadius,
                    dpToPx(3)
            )
            blurRadius =
                    typedArray.getDimension(R.styleable.BaseShadowLayout_blurRadius, dpToPx(12))
            enableShadow = typedArray.getBoolean(R.styleable.BaseShadowLayout_enableShadow, true)
            typedArray.recycle()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        clipPath.reset()

        clipRectF.top = 0f
        clipRectF.left = 0f
        clipRectF.right = canvas.width.toFloat()
        clipRectF.bottom = canvas.height.toFloat()
        clipPath.addRoundRect(clipRectF, cornerRadius, cornerRadius, Path.Direction.CW)

        canvas.clipPath(clipPath)
        super.dispatchDraw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        if (enableShadow) {
            drawShadow(canvas)
        }
//        drawRectBackground(canvas)
//        drawBorder(canvas)
        super.onDraw(canvas)
    }

    private fun drawBorder(canvas: Canvas) {

        val borderColor = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G900)

        borderPaint.style = Paint.Style.STROKE
        borderPaint.color = borderColor
        borderPaint.strokeWidth = 0.5f

        borderRectF.top = 0f
        borderRectF.left = 0f
        borderRectF.right = width.toFloat()
        borderRectF.bottom = height.toFloat()
        canvas.drawRoundRect(borderRectF, cornerRadius, cornerRadius, borderPaint)
    }


    private fun drawRectBackground(canvas: Canvas) {

        rectPaint.style = Paint.Style.FILL
        rectPaint.color = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        rectPaint.xfermode = porterDuffXfermode
        rectBackgroundRectF.top = 0f
        rectBackgroundRectF.left = 40f
        rectBackgroundRectF.right = width.toFloat() - 40f
        rectBackgroundRectF.bottom = height.toFloat()
        rectBackgroundPath.reset()
        rectBackgroundPath.addRect(rectBackgroundRectF, Path.Direction.CW)
        canvas.drawRoundRect(rectBackgroundRectF, cornerRadius, cornerRadius, rectPaint)
    }

    private fun drawRectBackground2(canvas: Canvas) {

        rectPaint.style = Paint.Style.FILL
        rectPaint.color = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        rectPaint.xfermode = porterDuffXfermode
        rectBackgroundRectF.top = 0f
        rectBackgroundRectF.left = 0f
        rectBackgroundRectF.right = width.toFloat()
        rectBackgroundRectF.bottom = height.toFloat()
        rectBackgroundPath.reset()
        rectBackgroundPath.addRect(rectBackgroundRectF, Path.Direction.CW)
        canvas.drawRoundRect(rectBackgroundRectF, cornerRadius, cornerRadius, rectPaint)
    }

    open fun drawShadow(canvas: Canvas) {
        canvas.save()
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.STROKE
        shadowPaint.color = shadowColor
        shadowPaint.strokeWidth = shadowStrokeWidth
        shadowPaint.xfermode = porterDuffXfermode

        shadowPaint.maskFilter = blurMaskFilter

        val yOffset = -shadowDy.toInt()
        val xOffset = -shadowDx.toInt()

        if (shadowStartY == java.lang.Float.MIN_VALUE) {
            shadowStartY = (height / 2).toFloat()
        }

        shadowPath.reset()
        shadowPath.moveTo((width + xOffset).toFloat(), shadowStartY)
        shadowPath.lineTo((-xOffset).toFloat(), shadowStartY)
        shadowPath.lineTo((-xOffset).toFloat(), (height + yOffset).toFloat())
        shadowPath.lineTo((width + xOffset).toFloat(), (height + yOffset).toFloat())
        shadowPath.lineTo((width + xOffset).toFloat(), shadowStartY)

        canvas.drawPath(shadowPath, shadowPaint)
        canvas.restore()
    }
}