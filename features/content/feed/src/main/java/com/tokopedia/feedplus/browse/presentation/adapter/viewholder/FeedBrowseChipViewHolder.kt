package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipBinding
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChipViewHolder private constructor(
    binding: ItemFeedBrowseChipBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private val chipView = binding.cuFeedBrowseLabel

    fun bind(item: FeedBrowseChipUiModel) {
        chipView.chipText = item.label

        onChipSelected(item)

        chipView.setOnClickListener {
            if (chipView.chipType == ChipsUnify.TYPE_SELECTED) return@setOnClickListener
            listener.onChipClicked(item)
        }

        chipView.addOnImpressionListener(item.impressHolder) {
            listener.onChipImpressed(item, bindingAdapterPosition)
        }
    }

    private fun onChipSelected(item: FeedBrowseChipUiModel) {
        val newChipType = if (item.isSelected) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
        if (chipView.chipType != newChipType) {
            chipView.chipType = newChipType
        }

        if (item.isSelected) {
            listener.onChipSelected(item, bindingAdapterPosition)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ): FeedBrowseChipViewHolder {
            return FeedBrowseChipViewHolder(
                ItemFeedBrowseChipBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    interface Listener {
        fun onChipImpressed(model: FeedBrowseChipUiModel, position: Int)

        fun onChipClicked(model: FeedBrowseChipUiModel)

        fun onChipSelected(model: FeedBrowseChipUiModel, position: Int)
    }
}
