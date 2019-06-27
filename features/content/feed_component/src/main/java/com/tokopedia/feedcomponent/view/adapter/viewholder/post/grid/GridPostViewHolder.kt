package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.kotlin.extensions.view.getDimens
import kotlinx.android.synthetic.main.item_post_grid.view.*

/**
 * @author by milhamj on 07/12/18
 */
class GridPostViewHolder(private val gridItemListener: GridPostAdapter.GridItemListener)
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

        setGridListPadding(element.itemList.size)
    }

    private fun setGridListPadding(listSize: Int) {
        if (listSize == 1) {
            itemView.gridList.setPadding(0, 0, 0, 0)
        } else {
            itemView.gridList.setPadding(
                    itemView.getDimens(R.dimen.dp_3),
                    0,
                    itemView.getDimens(R.dimen.dp_3),
                    0
            )
        }
    }
}