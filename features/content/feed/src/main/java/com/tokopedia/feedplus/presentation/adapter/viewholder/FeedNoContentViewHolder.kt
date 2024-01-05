package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostErrorBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedNoContentViewHolder(
    binding: ItemFeedPostErrorBinding,
    private val listener: FeedListener
) : AbstractViewHolder<FeedNoContentModel>(binding.root) {

    private val customErrorView = binding.root

    override fun bind(element: FeedNoContentModel?) {
        val data = element ?: return
        customErrorView.setIcon(data.imageId)
        customErrorView.setTitle(data.title)
        customErrorView.setDescription(data.subtitle)
        customErrorView.setButton(data.buttonText) {
            listener.changeTab()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_feed_error
    }
}
