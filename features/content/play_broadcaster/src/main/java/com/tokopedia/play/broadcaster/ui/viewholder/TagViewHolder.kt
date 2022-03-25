package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemTagBinding
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by jegul on 18/02/21
 */
class TagViewHolder private constructor(
        private val binding: ItemTagBinding,
        private val listener: Listener
) : BaseViewHolder(binding.root) {

    fun bind(item: TagRecommendationListAdapter.Model.Tag) {
        binding.chipsTag.chipText = item.data.tag

        binding.chipsTag.chipType = if (item.data.isChosen) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

        binding.chipsTag.setOnClickListener {
            listener.onTagClicked(item.data)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: TagViewHolder.Listener,
        ) : TagViewHolder {
            return TagViewHolder(
                ItemTagBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener
            )
        }
    }

    interface Listener {

        fun onTagClicked(tag: PlayTagUiModel)
    }
}