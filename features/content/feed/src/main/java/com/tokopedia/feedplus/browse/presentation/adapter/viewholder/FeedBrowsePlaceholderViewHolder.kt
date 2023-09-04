package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView
import com.tokopedia.feedplus.databinding.ItemFeedBrowsePlaceholderBinding

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowsePlaceholderViewHolder(
    binding: ItemFeedBrowsePlaceholderBinding
): RecyclerView.ViewHolder(binding.root) {

    private val placeholderView = binding.root

    fun bind(type: FeedBrowsePlaceholderView.Type) {
        placeholderView.show(type)
    }
}
