package com.tokopedia.hotel.common.presentation.widget

/**
 * @author by jessica on 2019-07-23
 */

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpanningLinearLayoutManager : LinearLayoutManager {

    private val horizontalSpace: Int
        get() = width - paddingRight - paddingLeft

    private val verticalSpace: Int
        get() = height - paddingBottom - paddingTop
    private var itemSpacing: Int = 0

    constructor(context: Context?, itemSpacing: Int = 0) : super(context) { this.itemSpacing = itemSpacing }

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean, itemSpacing: Int = 0) : super(context, orientation, reverseLayout) {
        this.itemSpacing = itemSpacing
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(c: Context, attrs: AttributeSet): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(c, attrs))
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(lp))
    }

    private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        if (orientation == HORIZONTAL) {
            layoutParams.width = Math.round(horizontalSpace / itemCount.toDouble()).toInt() - itemSpacing
        } else if (orientation == VERTICAL) {
            layoutParams.height = Math.round(verticalSpace / itemCount.toDouble()).toInt() - itemSpacing
        }
        return layoutParams
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }
}