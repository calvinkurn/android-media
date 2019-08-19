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
                if (childPosition in 0..2) top = offset // Top row cells, add top offset
                bottom = offset
                if (childPosition % 3 != 2) right = offset // Rightmost cell does not have right offset
            }
        }

    }

}