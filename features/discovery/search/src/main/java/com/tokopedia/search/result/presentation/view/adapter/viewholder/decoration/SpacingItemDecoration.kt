package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacingItemDecoration(private val dimen: Int) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = dimen
        outRect.top = dimen / 2
        outRect.bottom = dimen / 2
    }
}