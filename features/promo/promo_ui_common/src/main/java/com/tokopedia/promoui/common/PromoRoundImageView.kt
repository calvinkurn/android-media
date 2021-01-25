package com.tokopedia.promoui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class PromoRoundImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    var radius = 0f
    val path = Path()
    val rectF = RectF()

    init {
        attrs?.let { attributes ->
            val typedArray =
                    context.theme.obtainStyledAttributes(attributes, R.styleable.PromoRoundImageView, 0, 0)
            radius = typedArray.getDimension(R.styleable.PromoRoundImageView_promo_round_radius, dpToPx(8))
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (rectF.right > 0) {
            path.addRoundRect(rectF, radius, radius, Path.Direction.CW)
            canvas?.clipPath(path)
        }
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.top = 0f
        rectF.left = 0f
        rectF.right = w.toFloat()
        rectF.bottom = h.toFloat()
    }
}