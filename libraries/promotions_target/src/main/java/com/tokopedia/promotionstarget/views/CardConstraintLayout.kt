package com.tokopedia.targetpromotions


import android.content.Context
import android.graphics.*
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.DisplayMetrics
import com.tokopedia.promotionstarget.R

open class CardConstraintLayout : ConstraintLayout {

    protected var borderPaint = Paint()
    protected var rectPaint = Paint()
    protected var shadowPaint = Paint()

    protected var shadowPath = Path()
    protected var clipPath = Path()
    protected var rectBackgroundPath = Path()

    protected var clipRectF = RectF()
    protected var rectBackgroundRectF = RectF()
    protected var borderRectF = RectF()

    protected var porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)

    protected var shadowColor = Color.BLACK

    protected var shadowStrokeWidth = 15f

    protected var blurRadius = 50f
    protected var cornerRadius = 8f
    protected var shadowHeight = 0f
    protected var shadowDx = 0f
    protected var shadowDy = 0f
    protected var shadowStartY = java.lang.Float.MIN_VALUE
    protected var enableShadow = false
    protected lateinit var blurMaskFilter: BlurMaskFilter

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
        readAttrs(attrs)
        blurMaskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
    }

    private fun readAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray =
                    context.theme.obtainStyledAttributes(attrs, R.styleable.CardConstraintLayout, 0, 0)
            shadowHeight = typedArray.getDimension(R.styleable.CardConstraintLayout_shadowHeight, 0f)
            shadowDx =
                    typedArray.getDimension(R.styleable.CardConstraintLayout_shadowDx, dpToPx(context, 0))
            shadowDy =
                    typedArray.getDimension(R.styleable.CardConstraintLayout_shadowDy, dpToPx(context, 0))
            shadowStartY = typedArray.getDimension(
                    R.styleable.CardConstraintLayout_shadowStartY,
                    java.lang.Float.MIN_VALUE
            )
            shadowColor = typedArray.getColor(R.styleable.CardConstraintLayout_shadowColor, Color.BLACK)
            shadowStrokeWidth =
                    typedArray.getDimension(
                            R.styleable.CardConstraintLayout_shadowStrokeWidth,
                            dpToPx(context, 1)
                    )
            cornerRadius = typedArray.getDimension(
                    R.styleable.CardConstraintLayout_cornerRadius,
                    dpToPx(context, 3)
            )
            blurRadius =
                    typedArray.getDimension(R.styleable.CardConstraintLayout_blurRadius, dpToPx(context, 12))
            enableShadow = typedArray.getBoolean(R.styleable.CardConstraintLayout_enableShadow, true)
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
        drawRectBackground(canvas)
        drawBorder(canvas)
        super.onDraw(canvas)
    }

    private fun drawBorder(canvas: Canvas) {

        val borderColor = ContextCompat.getColor(context, R.color.t_promo_borderColor)

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
        rectPaint.color = Color.WHITE
        rectPaint.xfermode = porterDuffXfermode
        rectBackgroundRectF.top = 0f
        rectBackgroundRectF.left = 0f
        rectBackgroundRectF.right = width.toFloat()
        rectBackgroundRectF.bottom = height.toFloat()
        rectBackgroundPath.reset()
        rectBackgroundPath.addRect(rectBackgroundRectF, Path.Direction.CW)
        canvas.drawRoundRect(rectBackgroundRectF, cornerRadius, cornerRadius, rectPaint)
    }

    private fun drawShadow(canvas: Canvas) {
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

    fun dpToPx(context: Context, dp: Int): Float {
        return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
    }
}