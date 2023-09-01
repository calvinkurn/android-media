package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.delegate.ChipAdapterDelegate
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChipViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChipAdapter(
    listener: FeedBrowseChipViewHolder.Listener
): BaseDiffUtilAdapter<FeedBrowseChipUiModel>(isFlexibleType = true) {

    init {
        delegatesManager.addDelegate(ChipAdapterDelegate(listener))
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
}
