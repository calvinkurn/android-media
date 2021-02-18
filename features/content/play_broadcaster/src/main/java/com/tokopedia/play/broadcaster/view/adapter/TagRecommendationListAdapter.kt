package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.TagRecommendationAdapterDelegate

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationListAdapter(
        listener: TagRecommendationViewHolder.Listener
) : BaseDiffUtilAdapter<String>() {

    init {
        delegatesManager
                .addDelegate(TagRecommendationAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}