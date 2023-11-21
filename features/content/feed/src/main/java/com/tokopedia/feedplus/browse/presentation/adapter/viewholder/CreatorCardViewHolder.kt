package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCreatorBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCreatorLoadingBinding
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by meyta.taliti on 25/09/23.
 */
internal class CreatorCardViewHolder private constructor() {

    class Item private constructor(
        private val binding: ItemFeedBrowseCreatorBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val playWidget = binding.cvCreator

        private val dp8 =
            binding.root.context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl3)

        init {
            playWidget.setCornerRadius(
                topLeft = dp8.toFloat(),
                topRight = dp8.toFloat(),
                bottomLeft = 0f,
                bottomRight = 0f
            )
        }

        fun bind(item: PlayWidgetChannelUiModel) {
            playWidget.setData(item)

            binding.tvCreatorName.text = item.partner.name
            binding.ivCreatorProfilePic.loadImageCircle(item.video.coverUrl)

            playWidget.setOnClickListener {
                listener.onCreatorChannelCardClicked(this@Item, item)
            }

            binding.ivCreatorProfilePic.setOnClickListener {
                listener.onCreatorClicked(this, item)
            }

            binding.root.setOnClickListener {
                listener.onCreatorClicked(this, item)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Item {
                return Item(
                    ItemFeedBrowseCreatorBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                    listener,
                )
            }
        }

        interface Listener {
            fun onCreatorChannelCardClicked(
                viewHolder: Item,
                item: PlayWidgetChannelUiModel
            )

            fun onCreatorClicked(
                viewHolder: Item,
                item: PlayWidgetChannelUiModel
            )
        }
    }

    class Placeholder private constructor(
        binding: ItemFeedBrowseCreatorLoadingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Placeholder {
                return Placeholder(
                    ItemFeedBrowseCreatorLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }
}
