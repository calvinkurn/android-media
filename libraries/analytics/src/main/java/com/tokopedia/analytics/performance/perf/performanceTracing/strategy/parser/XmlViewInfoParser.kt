package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth

class XmlViewInfoParser() : ViewInfoParser<View> {
    @Suppress("SwallowedException")
    override suspend fun parse(view: View): ViewInfo {
        val tag = (view.tag as? String) ?: ""

        val height = view.height
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
}
