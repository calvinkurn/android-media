package com.tokopedia.banner.dynamic.adapter.decorator

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * @author by furqan on 13/09/2019
 */

class BannerDynamicViewDecorator(val leftFirst: Int,
                                 val left: Int,
                                 val rightLast: Int,
                                 val right: Int)
    : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager!!.getPosition(view) == 0) {
            outRect.left = leftFirst
        } else {
            outRect.left = left
        }

        if (parent.layoutManager!!.getPosition(view) == parent.layoutManager!!.itemCount - 1) {
            outRect.right = rightLast
        } else {
            outRect.right = right
        }
    }

}