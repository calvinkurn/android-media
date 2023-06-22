package com.tokopedia.feedplus.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.databinding.ItemFeedLoadMoreContentBinding

/**
 * Created By : Muhammad Furqan on 09/06/23
 */
class FeedLoadMoreViewHolder(
    binding: ItemFeedLoadMoreContentBinding
) : AbstractViewHolder<LoadingMoreModel>(binding.root) {
    override fun bind(element: LoadingMoreModel?) {}
}
