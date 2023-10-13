package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseCardAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChannelItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalChannelsBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class FeedBrowseHorizontalChannelsViewHolder private constructor(
    binding: ItemFeedBrowseHorizontalChannelsBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = FeedBrowseCardAdapter(
        object : FeedBrowseCardViewHolder.Listener {
            override fun onCardImpressed(item: PlayWidgetChannelUiModel, position: Int) {
            }

            override fun onCardClicked(item: PlayWidgetChannelUiModel, position: Int) {
            }
        }
    )

    init {
        binding.root.adapter = adapter
        binding.root.addItemDecoration(
            FeedBrowseChannelItemDecoration(
                context = binding.root.context,
                spacingHorizontal = R.dimen.feed_space_8,
                spacingTop = unifyprinciplesR.dimen.layout_lvl0
            )
        )
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalChannels) {
        adapter.submitList(item.channels)
    }

    fun bindPayloads(item: FeedBrowseItemListModel.HorizontalChannels, payloads: FeedBrowsePayloads) {
        if (payloads.isChannelItemsChanged()) adapter.submitList(item.channels)
    }

    companion object {
        fun create(
            parent: ViewGroup,
        ): FeedBrowseHorizontalChannelsViewHolder {
            return FeedBrowseHorizontalChannelsViewHolder(
                ItemFeedBrowseHorizontalChannelsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
