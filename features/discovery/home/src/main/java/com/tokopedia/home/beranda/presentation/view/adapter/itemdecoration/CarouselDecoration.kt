package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View


class CarouselDecoration(val marginLeft: Int, maxParentWidth: Int,
                         minimumVisibleItem: Int,
                         viewHolderWidth: Int) : RecyclerView.ItemDecoration() {
    var leftOutRect = 0

    init {
        val parentWidth = maxParentWidth - (viewHolderWidth*3/4)
        val leftOutRectTotal = parentWidth - (viewHolderWidth*minimumVisibleItem)
        leftOutRect = leftOutRectTotal / (minimumVisibleItem)
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.layoutManager?.getPosition(view)!! > 0) {
            outRect.left = leftOutRect
        } else {
            outRect.left = marginLeft
        }
    }
}
