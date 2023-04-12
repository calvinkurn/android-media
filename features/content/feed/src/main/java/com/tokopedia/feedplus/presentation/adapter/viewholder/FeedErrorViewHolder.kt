package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedNoContentBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedErrorViewHolder(
    private val binding: ItemFeedNoContentBinding,
    private val listener: FeedListener
) : AbstractViewHolder<ErrorNetworkModel>(binding.root) {

    override fun bind(element: ErrorNetworkModel?) {
        with(binding) {
            iconFeedNoContent.setImage(IconUnify.RELOAD)
            tyFeedNoContentTitle.text =
                root.context.getString(R.string.feed_label_error_fetch_title)
            tyFeedNoContentSubtitle.text =
                root.context.getString(R.string.feed_label_error_fetch_subtitle)
            btnShowOtherContent.text =
                root.context.getString(R.string.feed_label_error_fetch_button)
            btnShowOtherContent.setOnClickListener {
                listener.reload()
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_no_content
    }
}
