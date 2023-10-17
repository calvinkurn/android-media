package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseCardAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseHorizontalChannelsItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.hasContent
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalChannelsBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class FeedBrowseHorizontalChannelsViewHolder private constructor(
    private val binding: ItemFeedBrowseHorizontalChannelsBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = FeedBrowseCardAdapter(
        object : FeedBrowseChannelViewHolder2.Channel.Listener {
            override fun onCardImpressed(item: PlayWidgetChannelUiModel, position: Int) {
            }

            override fun onCardClicked(item: PlayWidgetChannelUiModel, position: Int) {
            }
        }
    )

    init {
        binding.root.adapter = adapter
        binding.root.itemAnimator = null
        binding.root.setHasFixedSize(true)
        binding.root.addItemDecoration(
            FeedBrowseHorizontalChannelsItemDecoration(binding.root.resources)
        )
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalChannels) {
        if (item.itemState.hasContent()) {
            adapter.submitList(item.itemState.items)
        } else {
            adapter.setLoading()
        }

        binding.root.scrollToPosition(0)
    }

    fun bindPayloads(item: FeedBrowseItemListModel.HorizontalChannels, payloads: FeedBrowsePayloads) {
        if (payloads.isChannelItemsChanged()) bind(item)
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
