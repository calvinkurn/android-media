package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class HomeRevampRecyclerDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = parent.layoutManager!!.itemCount - 1
        val currentPosition = parent.layoutManager!!.getPosition(view)
        if (currentPosition == (itemCount-1)) {
            outRect.bottom = spacing
        }
    }
}
