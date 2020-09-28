package com.tokopedia.promoui.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class CouponView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var shadowPaint = Paint()
    private var shadowPath = Path()
    private var shadowColor = Color.BLACK
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
    var bottomPadding = 0
    private val blurMaskFilter: BlurMaskFilter

    val bottomRoundPath = Path()
    val bottomRectF = RectF()
    val bottomPaint = Paint()

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
        setPadding(defaultPadding, 0, defaultPadding, defaultPadding)

        bottomPaint.style = Paint.Style.FILL
        bottomPaint.color = Color.WHITE
        bottomPaint.isAntiAlias = true

        shadowColor = ContextCompat.getColor(context, R.color.promo_ui_com_shadow_color)
    }


    override fun dispatchDraw(canvas: Canvas) {
        drawShadow(canvas)
        drawBottomRoundBg(canvas)
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
            bottomRoundPath.reset()
            bottomRectF.top = bottomOfBigImage
            bottomRectF.left = bottomPadding.toFloat()
            bottomRectF.right = width - bottomPadding.toFloat()
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

    fun rectShadowPath() {
        shadowPath.reset()
        shadowPath.moveTo((width + (shadowEndOffset)), shadowStartY + shadowTopOffset)              //Top Right
        shadowPath.lineTo((shadowStartOffset), shadowStartY + shadowTopOffset)                      // TR -> TL
        shadowPath.lineTo((shadowStartOffset), (height + shadowBottomOffset))                         // TL -> BL
        shadowPath.lineTo((width + shadowEndOffset), (height + shadowBottomOffset))                   // BL -> BR
        shadowPath.lineTo((width + shadowEndOffset), shadowStartY + shadowTopOffset)               // BR -> TR
    }
}

fun View.dpToPx(dp: Int): Float {
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
}