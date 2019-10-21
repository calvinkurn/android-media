package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * Created by errysuprayogi on 12/4/17.
 */

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int, private val excludeFirstLastItem: Boolean, private var start: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (excludeFirstLastItem) {
            if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1 && parent.getChildAdapterPosition(view) >= start) {
                outRect.bottom = verticalSpaceHeight
            }
        } else {
            outRect.bottom = verticalSpaceHeight
        }
    }

    fun setStart(start: Int) {
        this.start = start
    }
}