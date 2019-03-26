package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class DynamicIconDecoration(val marginLeft: Int, maxParentWidth: Int,
                             minimumVisibleIcon: Int,
                             iconWidth: Int) : RecyclerView.ItemDecoration() {
    var leftOutRect = 0

    init {
        val parentWidth = maxParentWidth - (iconWidth*3/4)
        val leftOutRectTotal = parentWidth - (iconWidth*minimumVisibleIcon)
        leftOutRect = leftOutRectTotal / (minimumVisibleIcon)
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.layoutManager.getPosition(view) > 0) {
            outRect.left = leftOutRect
        } else {
            outRect.left = marginLeft
        }
    }
}
