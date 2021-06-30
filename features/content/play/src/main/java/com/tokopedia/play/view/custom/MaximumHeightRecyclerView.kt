package com.tokopedia.play.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R

/**
 * Created by jegul on 09/01/20
 */
class MaximumHeightRecyclerView : RecyclerView {

    companion object {
        private const val DEFAULT_MAX_HEIGHT = -1f
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initAttrs(context, attrs)
    }

    private var mMaxHeight: Float = DEFAULT_MAX_HEIGHT

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MaximumHeightRecyclerView)

            mMaxHeight = attributeArray.getDimension(R.styleable.MaximumHeightRecyclerView_maximumHeight, DEFAULT_MAX_HEIGHT)
            attributeArray.recycle()
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val height = if (mMaxHeight >= 0f) MeasureSpec.makeMeasureSpec(mMaxHeight.toInt(), MeasureSpec.AT_MOST) else heightSpec
        super.onMeasure(widthSpec, height)
    }

    fun setMaxHeight(maxHeight: Float) {
        if (mMaxHeight == maxHeight) return
        mMaxHeight = maxHeight
        requestLayout()
        invalidate()
    }
}