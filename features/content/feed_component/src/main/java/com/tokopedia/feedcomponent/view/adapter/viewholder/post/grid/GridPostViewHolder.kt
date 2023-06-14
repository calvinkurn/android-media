package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostModel

/**
 * @author by milhamj on 07/12/18
 */
class GridPostViewHolder(private val gridItemListener: GridPostAdapter.GridItemListener) :
    BasePostViewHolder<GridPostModel>() {

    private val gridList: RecyclerView = itemView.findViewById(R.id.gridList)

    companion object {
        private const val SPAN_SIZE_FULL = 6
        private const val SPAN_SIZE_HALF = 3
        private const val SPAN_SIZE_SINGLE = 2
        private const val INDEX_SPAN_FULL = 1
        private const val INDEX_SPAN_HALF = 2
    }

    override var layoutRes: Int = R.layout.item_post_grid

    override fun bind(element: GridPostModel) {
        val layoutManager = GridLayoutManager(
            itemView.context,
            SPAN_SIZE_FULL,
            LinearLayoutManager.VERTICAL,
            false
        )
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (element.itemList.size) {
                    INDEX_SPAN_FULL -> SPAN_SIZE_FULL
                    INDEX_SPAN_HALF -> SPAN_SIZE_HALF
                    else -> SPAN_SIZE_SINGLE
                }
            }
        }
        gridList.layoutManager = layoutManager

        val adapter = GridPostAdapter(pagerPosition, element, gridItemListener)
        gridList.adapter = adapter

        setGridListPadding(element.itemList.size)
    }

    private fun setGridListPadding(listSize: Int) {
        if (listSize == 1) {
            gridList.setPadding(0, 0, 0, 0)
        } else {
            gridList.setPadding(
                itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1).toInt(),
                0,
                itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1).toInt(),
                0
            )
        }
    }
}
