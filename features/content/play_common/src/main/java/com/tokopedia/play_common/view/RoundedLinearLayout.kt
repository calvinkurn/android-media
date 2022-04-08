package com.tokopedia.play_common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.tokopedia.play_common.R

/**
 * Created by jegul on 30/06/21
 */
open class RoundedLinearLayout : LinearLayout {

    private val roundedHelper = RoundedLayoutHelper()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedLinearLayout)

            roundedHelper.setCornerRadius(
                    attributeArray.getDimension(R.styleable.RoundedLinearLayout_rll_cornerRadius, 0f)
            )
            attributeArray.recycle()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) roundedHelper.setupCorner(w.toFloat(), h.toFloat())
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun dispatchDraw(canvas: Canvas) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val save = canvas.save()
            canvas.clipPath(roundedHelper.cornerPath)
            super.dispatchDraw(canvas)
            canvas.restoreToCount(save)
        } else {
            setRoundedOutlineProvider(roundedHelper.cornerRadius)
            super.dispatchDraw(canvas)
        }
    }

    fun setCornerRadius(cornerRadius: Float) {
        roundedHelper.setCornerRadius(cornerRadius)
        invalidate()
        requestLayout()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setRoundedOutlineProvider(cornerRadius: Float) {
        outlineProvider = roundedHelper.getOutlineProvider(cornerRadius)
        clipToOutline = true
    }
}