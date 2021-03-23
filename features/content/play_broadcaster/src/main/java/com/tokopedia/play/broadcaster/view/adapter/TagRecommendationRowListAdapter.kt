package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.tag.TagRecommendationRowUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.TagRecommendationRowAdapterDelegate

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationRowListAdapter(
        listener: TagRecommendationViewHolder.Listener
) : BaseDiffUtilAdapter<TagRecommendationRowUiModel>() {

    init {
        delegatesManager
                .addDelegate(TagRecommendationRowAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: TagRecommendationRowUiModel, newItem: TagRecommendationRowUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TagRecommendationRowUiModel, newItem: TagRecommendationRowUiModel): Boolean {
        return oldItem == newItem
    }
}