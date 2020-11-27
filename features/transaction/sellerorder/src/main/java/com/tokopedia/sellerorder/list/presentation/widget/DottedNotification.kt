package com.tokopedia.sellerorder.list.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.partial_dotted_drawable.view.*

class DottedNotification(
        private val context: Context,
        @DrawableRes private val drawable: Int,
        private val showDot: Boolean) : Drawable() {
    override fun draw(canvas: Canvas) {
        canvas.translate(-14.toPx().toFloat(), -12.toPx().toFloat())
        LayoutInflater.from(context).inflate(com.tokopedia.sellerorder.R.layout.partial_dotted_drawable, null).apply {
            ivDrawable.loadImageDrawable(drawable)
            notificationDot.showWithCondition(showDot)
            measure(View.MeasureSpec.makeMeasureSpec(24.toPx(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(24.toPx(), View.MeasureSpec.EXACTLY))
            layout(0, 0, measuredWidth, measuredHeight)
        }.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }
}