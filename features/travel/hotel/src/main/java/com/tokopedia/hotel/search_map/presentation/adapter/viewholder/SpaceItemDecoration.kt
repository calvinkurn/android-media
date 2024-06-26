package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val spaceDepth: Int, private val orientation: Int): RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != (parent.adapter?.itemCount ?: 0) - 1){
            if (orientation == LinearLayoutManager.HORIZONTAL){
                outRect.right = spaceDepth
            } else {
                outRect.bottom = spaceDepth
            }
        }
    }
}