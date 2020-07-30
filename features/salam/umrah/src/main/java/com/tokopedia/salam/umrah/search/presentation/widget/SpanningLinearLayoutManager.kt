package com.tokopedia.salam.umrah.search.presentation.widget

/**
 * @author by M on 18/10/19
 */

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class SpanningLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean, private val margin: Int) : LinearLayoutManager(context, orientation, reverseLayout) {
    private val horizontalSpace: Int
        get() = width - 2*margin

    private val verticalSpace: Int
        get() = height - 2*margin

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
            layoutParams.width = (horizontalSpace / itemCount.toDouble()).roundToInt()
        } else if (orientation == VERTICAL) {
            layoutParams.height = (verticalSpace / itemCount.toDouble()).roundToInt()
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