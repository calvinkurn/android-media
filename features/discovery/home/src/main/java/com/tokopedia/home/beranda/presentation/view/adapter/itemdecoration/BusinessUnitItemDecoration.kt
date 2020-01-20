package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

/**
 * Created by errysuprayogi on 3/22/18.
 */

import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.home.R

class BusinessUnitItemDecoration @JvmOverloads constructor(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        setSpacingForDirection(outRect, position, itemCount, view)
    }

    private fun setSpacingForDirection(outRect: Rect,
                                       position: Int,
                                       itemCount: Int,
                                       view: View) {

        outRect.left = if (position == 0) view.resources.getDimensionPixelOffset(R.dimen.dp_8)  else spacing
        outRect.right = if (position == itemCount) view.resources.getDimensionPixelOffset(R.dimen.dp_8) else spacing
        outRect.top = spacing
        outRect.bottom = spacing
    }
}