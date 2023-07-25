package com.tokopedia.promousage.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tokopedia.promousage.R
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class TextDrawable(context: Context, private val text: String) : Drawable() {
    private val paint: Paint = Paint()
    private var fontType = Typography.getFontType(context, true, Typography.PARAGRAPH_2)


    init {
        paint.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        paint.textSize = context.resources.getDimension(R.dimen.textfield2_text_size)
        paint.typeface = fontType
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.LEFT

        setBounds(-(24 - 0.toPx()), 0, paint.measureText(text).toInt(), 24.toDp())
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
