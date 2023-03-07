package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedNoContentBinding
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel

/**
 * Created By : Muhammad Furqan on 08/03/23
 */
class FeedNoContentViewHolder(
    private val binding: ItemFeedNoContentBinding,
) : AbstractViewHolder<FeedNoContentModel>(binding.root) {

    override fun bind(element: FeedNoContentModel?) {
        // TODO : Implement Click Function
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_no_content
    }
}
