package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.TagAdapterDelegate

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationListAdapter(
        listener: TagViewHolder.Listener
) : BaseDiffUtilAdapter<PlayTagUiModel>() {

    init {
        delegatesManager
                .addDelegate(TagAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: PlayTagUiModel, newItem: PlayTagUiModel): Boolean {
        return oldItem.tag == newItem.tag
    }

    override fun areContentsTheSame(oldItem: PlayTagUiModel, newItem: PlayTagUiModel): Boolean {
        return oldItem == newItem
    }
}