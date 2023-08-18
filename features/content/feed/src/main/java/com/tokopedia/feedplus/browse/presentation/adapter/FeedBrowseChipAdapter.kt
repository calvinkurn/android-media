package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChipViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipBinding

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChipAdapter: BaseDiffUtilAdapter<FeedBrowseChipUiModel>(isFlexibleType = true) {

    init {
        delegatesManager.addDelegate(AdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: FeedBrowseChipUiModel,
        newItem: FeedBrowseChipUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FeedBrowseChipUiModel,
        newItem: FeedBrowseChipUiModel
    ): Boolean {
        return oldItem == newItem
    }

    internal class AdapterDelegate:
        TypedAdapterDelegate<FeedBrowseChipUiModel, FeedBrowseChipUiModel, FeedBrowseChipViewHolder>(
            com.tokopedia.feedplus.R.layout.item_feed_browse_chip
        ) {
        override fun onBindViewHolder(
            item: FeedBrowseChipUiModel,
            holder: FeedBrowseChipViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedBrowseChipViewHolder {
            return FeedBrowseChipViewHolder(
                ItemFeedBrowseChipBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}
