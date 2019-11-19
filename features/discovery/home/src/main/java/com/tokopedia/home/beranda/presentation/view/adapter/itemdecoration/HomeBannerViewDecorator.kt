package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class HomeBannerViewDecorator(private val leftFirst: Int,
                              private val left: Int,
                              private val rightLast: Int,
                              private val right: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
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
