package com.tokopedia.topchat.chatroom.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class BackgroundImageView : AppCompatImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val rectF = RectF()

    override fun onDraw(canvas: Canvas?) {
        val imageWidth = width - paddingLeft - paddingRight
        val imageHeight = height - paddingTop - paddingBottom
        if (background != null) {
            canvas?.let {
                background.draw(it)
            }
        }
        if (drawable != null && drawable is BitmapDrawable) {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val bitmapHeight = bitmap.height * imageWidth / bitmap.width
            rectF.apply {
                left = paddingLeft.toFloat()
                top = (paddingTop + imageHeight - bitmapHeight).toFloat()
                right = (paddingLeft + imageWidth).toFloat()
                bottom = (height - paddingBottom).toFloat()
            }
            canvas?.drawBitmap(bitmap, null, rectF, null)
        } else {
            super.onDraw(canvas)
        }
    }
}