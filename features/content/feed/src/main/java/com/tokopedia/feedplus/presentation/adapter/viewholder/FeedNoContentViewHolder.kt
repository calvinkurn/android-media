package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedNoContentBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel
import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedNoContentViewHolder(
    private val binding: ItemFeedNoContentBinding,
    private val listener: FeedListener
) : AbstractViewHolder<FeedNoContentModel>(binding.root) {

    override fun bind(element: FeedNoContentModel?) {
        with(binding) {
            iconFeedNoContent.setImage(IconUnify.IMAGE)
            tyFeedNoContentTitle.text =
                root.context.getString(R.string.feed_label_no_content_title)
            tyFeedNoContentSubtitle.text =
                root.context.getString(R.string.feed_label_no_content_subtitle)
            btnShowOtherContent.text =
                root.context.getString(R.string.feed_label_no_content_button)
            btnShowOtherContent.setOnClickListener {
                listener.changeTab()
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_no_content
    }
}
