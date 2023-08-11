package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.databinding.ItemFeedPlaySlotTabCardBinding
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChipViewHolder(
    private val binding: ItemFeedPlaySlotTabCardBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedBrowseChipUiModel) {
        binding.cuFeedPlayLabel.chipText = item.label
    }
}
