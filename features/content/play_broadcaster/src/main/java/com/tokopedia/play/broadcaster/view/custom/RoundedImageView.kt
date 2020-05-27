package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.play.broadcaster.R

/**
 * Created by jegul on 26/05/20
 */
class RoundedImageView : AppCompatImageView {

    private var cornerRadius: Float = 0f
    private val cornerPath = Path()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView)

            cornerRadius = attributeArray.getDimension(R.styleable.RoundedImageView_cornerRadius, 0f)
            attributeArray.recycle()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val save = canvas.save()
            canvas.clipPath(cornerPath)
            super.dispatchDraw(canvas)
            canvas.restoreToCount(save)

        } else {
            setRoundedOutlineProvider(cornerRadius)
            super.dispatchDraw(canvas)
        }
    }

    fun setCornerRadius(cornerRadius: Float) {
        this.cornerRadius = cornerRadius
        invalidate()
        requestLayout()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setRoundedOutlineProvider(cornerRadius: Float) {
        outlineProvider = RoundedViewOutlineProvider(cornerRadius)
        clipToOutline = true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class RoundedViewOutlineProvider(private val cornerRadius: Float) : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
        }
    }
}