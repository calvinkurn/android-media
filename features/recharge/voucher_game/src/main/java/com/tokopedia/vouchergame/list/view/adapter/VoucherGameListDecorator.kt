package com.tokopedia.vouchergame.list.view.adapter

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * Created by resakemal on 14/08/19.
 */
class VoucherGameListDecorator(val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.run {
            parent.let {
                val childPosition = it.getChildAdapterPosition(view)

                if (childPosition in 0..2) top = space // Top row cells, add top offset
                bottom = space
                right = space
            }
        }

    }

}