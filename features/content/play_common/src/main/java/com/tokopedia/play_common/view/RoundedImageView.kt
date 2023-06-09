package com.tokopedia.play_common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.play_common.R

/**
 * Created by jegul on 10/08/20
 */
class RoundedImageView : AppCompatImageView {

    private var cornerRadius: Float = 0f
    private val cornerPath = Path()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)

            cornerRadius =
                attributeArray.getDimension(R.styleable.RoundedImageView_rImg_cornerRadius, 0f)
            attributeArray.recycle()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        setRoundedOutlineProvider(cornerRadius)
        super.dispatchDraw(canvas)
    }

    fun setCornerRadius(cornerRadius: Float) {
        this.cornerRadius = cornerRadius
        invalidate()
        requestLayout()
    }


    private fun setRoundedOutlineProvider(cornerRadius: Float) {
        outlineProvider = RoundedViewOutlineProvider(cornerRadius)
        clipToOutline = true
    }

    inner class RoundedViewOutlineProvider(private val cornerRadius: Float) :
        ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
        }
    }
}
