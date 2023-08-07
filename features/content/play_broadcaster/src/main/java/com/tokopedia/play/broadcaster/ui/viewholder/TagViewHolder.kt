package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.databinding.ItemTagBinding
import com.tokopedia.play.broadcaster.databinding.ItemTagPlaceholderBinding
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by jegul on 18/02/21
 */
class TagViewHolder private constructor() {

    class Tag private constructor(
        private val binding: ItemTagBinding,
        private val listener: Listener
    ) : BaseViewHolder(binding.root) {

        fun bind(item: PlayTagItem) {
            binding.chipsTag.chipText = item.tag

            binding.chipsTag.chipType = if (item.isChosen) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

            binding.chipsTag.setOnClickListener {
                listener.onTagClicked(item)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) : Tag {
                return Tag(
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

            fun onTagClicked(tag: PlayTagItem)
        }
    }

    class Placeholder private constructor(
        binding: ItemTagPlaceholderBinding
    ) : BaseViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup) : Placeholder {
                return Placeholder(
                    ItemTagPlaceholderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }
}
