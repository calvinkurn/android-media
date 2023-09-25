package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCreatorBinding

/**
 * Created by meyta.taliti on 25/09/23.
 */
class FeedBrowseCreatorViewHolder private constructor(
    private val binding: ItemFeedBrowseCreatorBinding,
    private val listener: Listener
) {

    fun bind(item: FeedBrowseUiModel.Creator) {
        binding.pwBrowseCreator.setData(item.playCard)
        binding.tvBrowseCreatorName.text = item.playCard.partner.name
        binding.ivBrowseCreatorPicture.setImageUrl(item.playCard.video.coverUrl)

        binding.pwBrowseCreator.setOnClickListener {
            listener.onCreatorChannelCardClicked(this, item)
        }

        binding.ivBrowseCreatorPicture.setOnClickListener {
            listener.onCreatorPictureClicked(this, item)
        }

        binding.tvBrowseCreatorName.setOnClickListener {
            listener.onCreatorPictureClicked(this, item)
        }
    }

    interface Listener {
        fun onCreatorChannelCardClicked(
            viewHolder: FeedBrowseCreatorViewHolder,
            item: FeedBrowseUiModel.Creator
        )

        fun onCreatorPictureClicked(
            viewHolder: FeedBrowseCreatorViewHolder,
            item: FeedBrowseUiModel.Creator
        )
    }
}
