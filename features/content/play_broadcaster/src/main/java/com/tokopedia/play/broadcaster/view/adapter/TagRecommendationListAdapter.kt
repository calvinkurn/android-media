package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.TagAdapterDelegate

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationListAdapter(
    listener: TagViewHolder.Tag.Listener
) : BaseDiffUtilAdapter<Any>() {

    init {
        delegatesManager
                .addDelegate(TagAdapterDelegate.Tag(listener))
                .addDelegate(TagAdapterDelegate.Placeholder())
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if(oldItem is PlayTagUiModel && newItem is PlayTagUiModel)
            return oldItem.tag == newItem.tag
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }
}