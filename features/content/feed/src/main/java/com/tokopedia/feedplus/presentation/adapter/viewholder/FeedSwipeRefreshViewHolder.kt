package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedSwipeRefreshBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedSwipeRefreshListener
import com.tokopedia.feedplus.presentation.model.FeedSwipeRefreshModel

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedSwipeRefreshViewHolder(
    private val binding: ItemFeedSwipeRefreshBinding,
    private val listener: FeedSwipeRefreshListener
) : AbstractViewHolder<FeedSwipeRefreshModel>(binding.root) {

    override fun bind(element: FeedSwipeRefreshModel?) {
        listener.setPullToRefreshItem(binding.feedPullRefreshIcon)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_swipe_refresh
    }
}
