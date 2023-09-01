package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipBinding
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChipViewHolder(
    binding: ItemFeedBrowseChipBinding,
    private val listener: Listener
): RecyclerView.ViewHolder(binding.root) {

    private val chipView = binding.cuFeedBrowseLabel

    fun bind(item: FeedBrowseChipUiModel) {
        chipView.chipText = item.label
        chipView.setOnClickListener {
            if (chipView.chipType == ChipsUnify.TYPE_SELECTED) {
                chipView.chipType = ChipsUnify.TYPE_NORMAL
            } else {
                chipView.chipType = ChipsUnify.TYPE_SELECTED
            }
        }
    }

    interface Listener {
    }
}
