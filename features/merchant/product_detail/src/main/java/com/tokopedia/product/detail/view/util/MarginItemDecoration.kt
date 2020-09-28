package com.tokopedia.product.detail.view.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 22/09/20
 */
class MarginItemDecoration(val left: Int, val top: Int, val right: Int, val bottom: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (position != 0) {
            parent.adapter?.also {
                outRect.set(left.toPx(), top.toPx(), right.toPx(), bottom.toPx())
            }
        }
    }
}