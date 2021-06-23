package com.tokopedia.play.broadcaster.ui.viewholder

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemTagBinding
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by jegul on 18/02/21
 */
class TagViewHolder(
        private val binding: ItemTagBinding,
        private val listener: Listener
) : BaseViewHolder(binding.root) {

    fun bind(item: PlayTagUiModel) {
        binding.chipsTag.chipText = item.tag

        binding.chipsTag.chipType = if (item.isChosen) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

        binding.chipsTag.setOnClickListener {
            listener.onTagClicked(item)
        }
    }

    companion object {

        val LAYOUT = R.layout.item_tag
    }

    interface Listener {

        fun onTagClicked(tag: PlayTagUiModel)
    }
}