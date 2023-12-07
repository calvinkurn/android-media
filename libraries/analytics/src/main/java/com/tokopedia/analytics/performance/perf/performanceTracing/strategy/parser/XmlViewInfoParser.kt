package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth

class XmlViewInfoParser() : ViewInfoParser<View> {
    @Suppress("SwallowedException")
    override suspend fun parse(view: View): ViewInfo {
        val tag = (view.tag as? String) ?: ""

        val height = getViewHeight(view)
        val isVisible = view.visibility == View.VISIBLE && view.isShown && height != 0 && isViewInViewport(view)
        val location = IntArray(2)
        view.getLocationOnScreen(location)

        val viewInfo = ViewInfo(
            name = view.javaClass.simpleName,
            tag = tag,
            isVisible = isVisible,
            height = height,
            location = location
        )

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val childView: View? = view.getChildAt(i)
                childView?.let {
                    val childViewInfo = parse(childView)
                    viewInfo.directChilds += childViewInfo
                }
            }
        }

        return viewInfo
    }

    fun isViewInViewport(view: View): Boolean {
        val offset = 100

        if (!view.isShown) return false
        val screen = Rect(0, 0, getScreenWidth(), getScreenHeight())
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val X = location[0] + offset
        val Y = location[1] + offset
        return screen.top <= Y && screen.bottom >= Y &&
            screen.left <= X && screen.right >= X
    }

    private fun RecyclerView.calculateRecyclerViewHeight(): Int {
        var totalHeight = 0

        for (i in 0 until this.childCount) {
            val child: View? = this.getChildAt(i)
            child?.let { totalHeight += child.height }
        }

        // Add the height of the RecyclerView's padding
        totalHeight += this.paddingTop + this.paddingBottom

        return totalHeight
    }

    private fun getViewHeight(view: View): Int {
        return when (view) {
            is RecyclerView -> view.calculateRecyclerViewHeight()
            else -> view.height
        }
    }

    private fun getChildCount(view: View): Int {
        return when (view) {
            is ViewGroup -> view.childCount
            else -> 0
        }
    }
}
