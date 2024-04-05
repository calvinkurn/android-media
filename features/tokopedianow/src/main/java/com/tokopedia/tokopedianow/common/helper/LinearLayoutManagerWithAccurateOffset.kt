package com.tokopedia.tokopedianow.common.helper

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Note: this custom LinearLayoutManager is used to handle wrong calculation when calling recyclerView.computeVerticalScrollOffset().
 * Source: https://stackoverflow.com/questions/30361403/recyclerview-linearlayoutmanager-computeverticalscrolloffset-not-returning-cor
 */

class LinearLayoutManagerWithAccurateOffset(
    context: Context?
) : LinearLayoutManager(context) {
    private val childSizesMap = mutableMapOf<Int, Int>()

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        for (i in Int.ZERO until childCount) {
            val child = getChildAt(i)
            if (child != null) childSizesMap[getPosition(child)] = child.height
        }
    }

    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
        if (childCount == Int.ZERO) {
            return Int.ZERO
        }

        val firstChildPosition = findFirstVisibleItemPosition()
        val firstChild = findViewByPosition(firstChildPosition)
        return if (firstChild != null) {
            var scrolledY: Int = -(firstChild.y.toInt())
            for (i in Int.ZERO until firstChildPosition) {
                scrolledY += childSizesMap[i].orZero()
            }
            scrolledY
        } else {
            Int.ZERO
        }
    }
}
