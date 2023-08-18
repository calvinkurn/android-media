package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseCardViewHolder
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardAdapter: BaseDiffUtilAdapter<PlayWidgetChannelUiModel>(isFlexibleType = true) {

    init {
        delegatesManager.addDelegate(AdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: PlayWidgetChannelUiModel,
        newItem: PlayWidgetChannelUiModel
    ): Boolean {
        return oldItem.channelId == newItem.channelId
    }

    override fun areContentsTheSame(
        oldItem: PlayWidgetChannelUiModel,
        newItem: PlayWidgetChannelUiModel
    ): Boolean {
        return oldItem == newItem
    }

    internal class AdapterDelegate:
        TypedAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetChannelUiModel, FeedBrowseCardViewHolder>(
            com.tokopedia.feedplus.R.layout.item_feed_browse_card
        ) {
        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: FeedBrowseCardViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedBrowseCardViewHolder {
            return FeedBrowseCardViewHolder(
                ItemFeedBrowseCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}
