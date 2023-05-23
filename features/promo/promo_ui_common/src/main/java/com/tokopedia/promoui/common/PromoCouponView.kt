package com.tokopedia.promoui.common

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class PromoCouponView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var shadowPaint = Paint()
    private var shadowPath = Path()
    private var shadowColor = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G900)
    private var shadowStrokeWidth = 15f
    private var porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    private var blurRadius = 0f

    private var shadowStartOffset = 0f
    private var shadowEndOffset = 0f
    private var shadowTopOffset = 0f
    private var shadowBottomOffset = 0f
    private var shadowStartY = java.lang.Float.MIN_VALUE
    var shadowRadius = 0f
    var commonOffset = 0f
    var cornerRadius = 0f
    var defaultPadding = 0
    var topPadding = 0
    var bottomPadding = 0
    private val blurMaskFilter: BlurMaskFilter

    val bottomRoundPath = Path()
    val bottomRectF = RectF()
    val bottomPaint = Paint()

    val VIEW_TYPE_NORMAL = 0
    val VIEW_TYPE_MVC = 1
    var viewType = VIEW_TYPE_NORMAL

    init {

        cornerRadius = dpToPx(8)
        shadowRadius = dpToPx(0)

        defaultPadding = dpToPx(6).toInt()
        bottomPadding = defaultPadding

        blurRadius = dpToPx(8)
        commonOffset = blurRadius + defaultPadding - dpToPx(1)

        shadowTopOffset = dpToPx(6)
        shadowBottomOffset = -commonOffset
        shadowStartOffset = commonOffset
        shadowEndOffset = -commonOffset

        blurMaskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.OUTER)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        bottomPaint.style = Paint.Style.FILL
        bottomPaint.color = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        bottomPaint.isAntiAlias = true

        shadowColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
        readDataFromAttrs(attrs)
        configure()

        setPadding(defaultPadding, topPadding, defaultPadding, defaultPadding)
    }

    private fun configure() {
        when (viewType) {
            VIEW_TYPE_MVC -> {
                shadowStartY = 0f
                cornerRadius = 0f
                topPadding = defaultPadding
                shadowTopOffset += dpToPx(4)
                shadowStrokeWidth = dpToPx(3)
                shadowColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
            }
        }
    }

    private fun readDataFromAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PromoCouponView, 0, 0)
            try {
                viewType = typedArray.getInt(R.styleable.PromoCouponView_promo_coupon_view_type, VIEW_TYPE_NORMAL)
            } finally {
                typedArray.recycle();
            }
        }
    }


    override fun dispatchDraw(canvas: Canvas) {
        drawShadow(canvas)
        if (viewType == VIEW_TYPE_NORMAL) {
            drawBottomRoundBg(canvas)
        }
        super.dispatchDraw(canvas)
    }

    fun drawBottomRoundBg(canvas: Canvas) {

        var bottomOfBigImage = 0f
        if (childCount > 0) {
            val range = 0 until childCount
            for (i in range) {
                if (getChildAt(i) is CouponImageView) {
                    bottomOfBigImage = getChildAt(i).bottom.toFloat()
                    break
                }
            }
            val subtractForMvcType = if (viewType == VIEW_TYPE_MVC) dpToPx(10) else 0f
            bottomRoundPath.reset()
            bottomRectF.top = bottomOfBigImage + topPadding
            bottomRectF.left = bottomPadding.toFloat() + subtractForMvcType
            bottomRectF.right = width - bottomPadding.toFloat() - subtractForMvcType
            bottomRectF.bottom = height - bottomPadding.toFloat()
            val radii = floatArrayOf(0f, 0f, 0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
            bottomRoundPath.addRoundRect(bottomRectF, radii, Path.Direction.CW)

            canvas.drawPath(bottomRoundPath, bottomPaint)
        }

    }

    fun drawShadow(canvas: Canvas) {
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.STROKE
        shadowPaint.color = shadowColor
        shadowPaint.strokeWidth = shadowStrokeWidth
        shadowPaint.xfermode = porterDuffXfermode

        shadowPaint.maskFilter = blurMaskFilter

        if (shadowStartY == java.lang.Float.MIN_VALUE) {
            shadowStartY = (height / 2).toFloat()
        }

        roundedShadowPath()
        canvas.drawPath(shadowPath, shadowPaint)
    }

    fun roundedShadowPath() {
        shadowPath.reset()
        val left = shadowStartOffset
        val top = shadowStartY + shadowTopOffset
        val right = width + shadowEndOffset
        val bottom = height + shadowBottomOffset
        val rectF = RectF(left.toFloat(), top, right, bottom.toFloat())
        shadowPath.addRoundRect(rectF, shadowRadius, shadowRadius, Path.Direction.CW)
    }
}

fun dpToPx(dp: Int): Float {
    return (dp * Resources.getSystem().displayMetrics.density)
}