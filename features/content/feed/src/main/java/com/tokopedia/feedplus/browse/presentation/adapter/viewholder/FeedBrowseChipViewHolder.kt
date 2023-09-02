package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.util.Log
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
        chipView.selectedChangeListener = { isActive ->
            if (isActive) listener.onChipSelected(item)
        }

        updateChip(item.isSelected)

        chipView.setOnClickListener {
            if (chipView.chipType == ChipsUnify.TYPE_SELECTED) return@setOnClickListener
            listener.onChipClicked(item)
        }
    }

    private fun updateChip(isSelected: Boolean) {
        val newChipType = if (isSelected) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
        chipView.chipType = newChipType
    }

    interface Listener {
        fun onChipClicked(model: FeedBrowseChipUiModel)

        fun onChipSelected(model: FeedBrowseChipUiModel)
    }
}
