package com.tokopedia.feedplus.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostErrorBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedErrorViewHolder(
    binding: ItemFeedPostErrorBinding,
    private val listener: FeedListener
) : AbstractViewHolder<ErrorNetworkModel>(binding.root) {

    private val customErrorView = binding.root

    override fun bind(element: ErrorNetworkModel?) {
        customErrorView.setIcon(IconUnify.SIGNAL_INACTIVE)
        customErrorView.setTitle(getString(R.string.feed_label_error_fetch_title))
        customErrorView.setDescription(getString(R.string.feed_label_error_fetch_subtitle))
        customErrorView.setButton(getString(R.string.feed_label_error_fetch_button)) {
            listener.reload()
        }
    }
}
