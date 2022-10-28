package com.tokopedia.videoTabComponent.view.custom

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.videoTabComponent.viewmodel.VideoTabAdapter
import kotlinx.android.synthetic.main.feed_sticky_header_recycler_view.view.*

class FeedHeaderItemDecoration(val parent: FeedPlayStickyHeaderRecyclerView,
private val isHeader: (itemPosition: Int) -> Boolean
) : RecyclerView.ItemDecoration() {
    init {
        parent.recycler_view.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                getRecyclerAdapter().setCurrentHeader(null)
            }
        })
    }
    fun getRecyclerAdapter() = (parent.recycler_view.adapter as VideoTabAdapter)

    override fun onDrawOver(c: Canvas, parent: RecyclerView) {
        super.onDrawOver(c, parent)
        val topChild = parent.getChildAt(0) ?: return
        var topChildPosition = parent.getChildAdapterPosition(topChild)

        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }
        getHeaderViewForItem(topChildPosition, parent)?.let {
            this.parent.addHeaderRecyclerView(it)
        } ?: also {
            this.parent.removeHeaderRecyclerView()
            getRecyclerAdapter().setCurrentHeader(null)
        }

    }
    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
        if (parent.adapter == null) {
            return null
        }

        val headerPosition = getHeaderPositionForItem(itemPosition)
        if (headerPosition == RecyclerView.NO_POSITION) return null
        val headerType = parent.adapter?.getItemViewType(headerPosition) ?: return null
        if (getRecyclerAdapter().getCurrentHeader()?.second?.itemViewType == headerType) {
            return getRecyclerAdapter().getCurrentHeader()?.second?.itemView
        }
        val headerHolder = parent.adapter?.createViewHolder(parent, headerType)
        if (headerHolder != null) {
            parent.adapter?.onBindViewHolder(headerHolder, headerPosition)
            getRecyclerAdapter().setCurrentHeader(headerPosition to headerHolder)
        }
        return headerHolder?.itemView
    }

    private fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = RecyclerView.NO_POSITION
        var currentPosition = itemPosition
        do {
            if (isHeader(currentPosition)) {
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)
        return headerPosition
    }
}
