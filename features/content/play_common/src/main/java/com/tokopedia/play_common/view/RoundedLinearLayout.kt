package com.tokopedia.play_common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
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
        if (attrs == null) return
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedLinearLayout)

        val wholeRadius = attributeArray.getDimension(
            R.styleable.RoundedLinearLayout_rll_cornerRadius, 0f
        )
        roundedHelper.setCornerRadius(wholeRadius)

        val topLeft = attributeArray.getDimension(
            R.styleable.RoundedLinearLayout_rll_topLeftRadius, -1f
        )
        val topRight = attributeArray.getDimension(
            R.styleable.RoundedLinearLayout_rll_topRightRadius, -1f
        )
        val bottomLeft = attributeArray.getDimension(
            R.styleable.RoundedLinearLayout_rll_bottomLeftRadius, -1f
        )
        val bottomRight = attributeArray.getDimension(
            R.styleable.RoundedLinearLayout_rll_bottomRightRadius, -1f
        )
        roundedHelper.setCornerRadius(
            topLeft = if (topLeft != -1f) topLeft else wholeRadius,
            topRight = if (topRight != -1f) topRight else wholeRadius,
            bottomLeft = if (bottomLeft != -1f) bottomLeft else wholeRadius,
            bottomRight = if (bottomRight != -1f) bottomRight else wholeRadius,
        )

        attributeArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        roundedHelper.setupCorner(w.toFloat(), h.toFloat())
    }

    override fun draw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(roundedHelper.cornerPath)
        super.draw(canvas)
        canvas.restoreToCount(save)
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun dispatchDraw(canvas: Canvas) {
        setRoundedOutlineProvider()
        super.dispatchDraw(canvas)
    }

    fun setCornerRadius(cornerRadius: Float) {
        roundedHelper.setCornerRadius(cornerRadius)
        invalidate()
        requestLayout()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setRoundedOutlineProvider() {
        outlineProvider = roundedHelper.getOutlineProvider()
        clipToOutline = true
    }
}