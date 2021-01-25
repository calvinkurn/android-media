package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.gamification.R
import com.tokopedia.promoui.common.CouponView
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class GreenGradientButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val layout = R.layout.gami_green_gradient_button
    var btn: Typography
    val shadowPadding = 6.toDp()

    //Paint props
    private var shadowPaint = Paint()
    private var shadowColor = Color.BLACK
    var shadowPath = Path()
    var shadowStartOffset = 0f
    var shadowEndOffset = 0f
    var shadowBottomOffset = 0f
    var shadowStrokeWidth = 0f
    var shadowRadius = 0f
    private var blurRadius = 0f
    private var porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    var blurMaskFilter: BlurMaskFilter?=null

    init {

        View.inflate(context, layout, this)
        btn = findViewById(R.id.cta)

        val paddingTop = context.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_green_gradient_btn_top_padding)?.toInt() ?: 0
        val paddingSide = context.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_green_gradient_btn_side_padding)?.toInt() ?: 0
        val lp = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        lp.apply {
            setPadding(paddingSide, paddingTop, paddingSide, paddingTop + shadowPadding)
        }
        layoutParams = lp

        initPaintProps()
    }

    fun initPaintProps(){
        shadowStrokeWidth = 3.toPx().toFloat()
        shadowBottomOffset = 8.toPx().toFloat()
        blurRadius = 16.toPx().toFloat()
        shadowColor = ContextCompat.getColor(context, com.tokopedia.promoui.common.R.color.promo_ui_com_shadow_color)

        blurMaskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.OUTER)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val commonOffset = blurRadius - dpToPx(1)
        shadowBottomOffset = -commonOffset
        shadowStartOffset = commonOffset
        shadowEndOffset = -commonOffset
    }

    fun getBg(): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.gami_green_grad_btn_padding)
    }

    fun setText(text: String?) {
        btn.text = text
    }

    override fun dispatchDraw(canvas: Canvas) {
        drawShadow(canvas)
        drawDrawable(canvas)
        super.dispatchDraw(canvas)
    }

    fun drawDrawable(canvas: Canvas){
        val bgDrawable = getBg()
        bgDrawable?.setBounds(0, 0, width, height)
        bgDrawable?.draw(canvas)
    }

    fun drawShadow(canvas: Canvas) {
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.STROKE
        shadowPaint.color = shadowColor
        shadowPaint.strokeWidth = shadowStrokeWidth
        shadowPaint.xfermode = porterDuffXfermode

        shadowPaint.maskFilter = blurMaskFilter

        roundedShadowPath()
        canvas.drawPath(shadowPath, shadowPaint)
    }

    fun roundedShadowPath() {
        shadowPath.reset()
        val left = shadowStartOffset
        val top = 0f
        val right = width + shadowEndOffset
        val bottom = height - shadowBottomOffset
        val rectF = RectF(left.toFloat(), top, right, bottom.toFloat())
        shadowPath.addRoundRect(rectF, shadowRadius, shadowRadius, Path.Direction.CW)
    }

}