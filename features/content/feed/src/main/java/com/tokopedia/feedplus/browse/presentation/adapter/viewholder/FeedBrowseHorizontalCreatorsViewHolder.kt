package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.presentation.adapter.CreatorAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.FeedBrowseHorizontalChannelsItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalCreatorsBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 16/11/23.
 */
internal class FeedBrowseHorizontalCreatorsViewHolder private constructor(
    binding: ItemFeedBrowseHorizontalCreatorsBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = CreatorAdapter(object : CreatorCardViewHolder.Item.Listener {
        override fun onCreatorChannelCardClicked(
            viewHolder: CreatorCardViewHolder.Item,
            item: PlayWidgetChannelUiModel
        ) {
            listener.onCreatorChannelCardClicked(this@FeedBrowseHorizontalCreatorsViewHolder, item)
        }

        override fun onCreatorClicked(
            viewHolder: CreatorCardViewHolder.Item,
            item: PlayWidgetChannelUiModel
        ) {
            listener.onCreatorClicked(this@FeedBrowseHorizontalCreatorsViewHolder, item)
        }
    })

    init {
        binding.rvCreators.adapter = adapter
        binding.rvCreators.setHasFixedSize(true)
        binding.rvCreators.addItemDecoration(
            FeedBrowseHorizontalChannelsItemDecoration(binding.rvCreators.resources)
        )
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalCreator) {
        when (item.itemState.state) {
            is ResultState.Fail -> {
                // TODO("should we handle error state?")
            }
            ResultState.Loading -> {
                adapter.setLoading()
            }
            ResultState.Success -> {
                adapter.submitList(item.itemState.items)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener
        ) : FeedBrowseHorizontalCreatorsViewHolder {
            return FeedBrowseHorizontalCreatorsViewHolder(
                ItemFeedBrowseHorizontalCreatorsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    interface Listener {

        fun onCreatorChannelCardClicked(
            viewHolder: FeedBrowseHorizontalCreatorsViewHolder,
            item: PlayWidgetChannelUiModel
        )

        fun onCreatorClicked(
            viewHolder: FeedBrowseHorizontalCreatorsViewHolder,
            item: PlayWidgetChannelUiModel
        )
    }
}
