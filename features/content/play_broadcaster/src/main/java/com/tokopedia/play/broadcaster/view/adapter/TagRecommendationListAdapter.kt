package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.TagRecommendationAdapterDelegate

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationListAdapter(
        listener: TagRecommendationViewHolder.Listener
) : BaseDiffUtilAdapter<PlayTagUiModel>() {

    init {
        delegatesManager
                .addDelegate(TagRecommendationAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: PlayTagUiModel, newItem: PlayTagUiModel): Boolean {
        return oldItem.tag == newItem.tag
    }

    override fun areContentsTheSame(oldItem: PlayTagUiModel, newItem: PlayTagUiModel): Boolean {
        return oldItem == newItem
    }
}