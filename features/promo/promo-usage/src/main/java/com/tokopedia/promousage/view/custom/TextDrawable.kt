package com.tokopedia.promousage.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tokopedia.promousage.R
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography

internal class TextDrawable(context: Context, private val text: String) : Drawable() {
    private val paint: Paint = Paint()

    init {
        paint.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        paint.textSize = context.resources.getDimension(R.dimen.promo_usage_cta_use_voucher_code_text_size)
        paint.typeface = Typography.getFontType(context, true, Typography.PARAGRAPH_2)
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.LEFT

        setBounds((-24).toDp(), 0, paint.measureText(text).toInt(), 24.toDp())
    }

    override fun draw(canvas: Canvas) {
        canvas.drawText(text, 0f, 18f, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

}
