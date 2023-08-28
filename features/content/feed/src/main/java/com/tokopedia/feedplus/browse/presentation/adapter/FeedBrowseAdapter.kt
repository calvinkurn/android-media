package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseAdapter: BaseDiffUtilAdapter<FeedBrowseUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
            .addDelegate(FeedBrowseAdapterDelegate.Placeholder())
            .addDelegate(FeedBrowseAdapterDelegate.Chips())
            .addDelegate(FeedBrowseAdapterDelegate.Cards())
    }

    override fun areItemsTheSame(oldItem: FeedBrowseUiModel, newItem: FeedBrowseUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FeedBrowseUiModel,
        newItem: FeedBrowseUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
