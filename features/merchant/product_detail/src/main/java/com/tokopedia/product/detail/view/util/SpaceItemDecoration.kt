package com.tokopedia.product.detail.view.util

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceItemDecoration(private val spaceDepth: Int, private val orientation: Int): RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        if (parent?.getChildAdapterPosition(view) != (parent?.adapter?.itemCount ?: 0) - 1){
            if (orientation == LinearLayoutManager.HORIZONTAL){
                outRect?.right = spaceDepth
            } else {
                outRect?.bottom = spaceDepth
            }
        }
    }
}