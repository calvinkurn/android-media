package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import kotlinx.android.synthetic.main.item_post_grid.view.*

/**
 * @author by milhamj on 07/12/18
 */
class GridPostViewHolder(val gridItemListener: GridPostAdapter.GridItemListener)
    : BasePostViewHolder<GridPostViewModel>() {

    companion object {
        private const val SPAN_SIZE_FULL = 6
        private const val SPAN_SIZE_HALF = 3
        private const val SPAN_SIZE_SINGLE = 2
    }

    override var layoutRes: Int = R.layout.item_post_grid

    override fun bind(element: GridPostViewModel) {
        val layoutManager = GridLayoutManager(
                itemView.context,
                SPAN_SIZE_FULL,
                LinearLayoutManager.VERTICAL,
                false
        )
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(element.itemList.size) {
                    1 -> SPAN_SIZE_FULL
                    2 -> SPAN_SIZE_HALF
                    else -> SPAN_SIZE_SINGLE
                }
            }
        }
        itemView.gridList.layoutManager = layoutManager

        val adapter = GridPostAdapter(pagerPosition, element, gridItemListener)
        itemView.gridList.adapter = adapter
    }
}