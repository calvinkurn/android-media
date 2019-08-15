package com.tokopedia.vouchergame.list.view.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View

/**
 * Created by resakemal on 14/08/19.
 */
class VoucherGameListDecorator(val space: Int, val resources: Resources) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.run {
            parent.let {
                val childPosition = it.getChildAdapterPosition(view)
                val offset = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        space.toFloat(),
                        resources.displayMetrics
                ).toInt()
                if (childPosition >= 0 || childPosition <= 2) top = offset // Top row cells, add top offset
                if (childPosition % 3 == 0) { // Even index cell, add left offset
                    left = offset
                }
                bottom = offset
                right = offset
            }
        }

    }

}