package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipBinding

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChipViewHolder(
    private val binding: ItemFeedBrowseChipBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedBrowseChipUiModel) {
        binding.cuFeedBrowseLabel.chipText = item.label
    }
}
