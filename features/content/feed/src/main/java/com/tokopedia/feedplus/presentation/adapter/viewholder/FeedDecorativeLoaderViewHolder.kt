package com.tokopedia.feedplus.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.databinding.ItemFeedDecorativeLoaderBinding

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedDecorativeLoaderViewHolder(
    binding: ItemFeedDecorativeLoaderBinding
) : AbstractViewHolder<LoadingModel>(binding.root) {

    override fun bind(element: LoadingModel?) {}
}
