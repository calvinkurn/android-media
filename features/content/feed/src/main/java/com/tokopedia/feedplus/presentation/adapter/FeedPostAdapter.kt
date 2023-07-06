package com.tokopedia.feedplus.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE
import com.tokopedia.feedplus.presentation.util.FeedDiffUtilCallback

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostAdapter(typeFactory: FeedAdapterTypeFactory) :
    BaseAdapter<FeedAdapterTypeFactory>(typeFactory, mutableListOf()) {

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(FeedDiffUtilCallback(visitables, newList))
        diffResult.dispatchUpdatesTo(this)

        setElements(newList)
    }

    fun showClearView(position: Int) {
        if ((list?.size ?: 0) > position) {
            notifyItemChanged(position, FEED_POST_CLEAR_MODE)
        }
    }

    override fun showLoading() {
        try {
            super.showLoading()
        } catch (_: Throwable) {
        }
    }
}
