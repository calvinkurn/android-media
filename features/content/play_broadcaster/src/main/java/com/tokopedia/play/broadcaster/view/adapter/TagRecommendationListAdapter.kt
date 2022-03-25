package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.TagAdapterDelegate
import com.tokopedia.play.broadcaster.view.adapter.delegate.TagPlaceholderAdapterDelegate

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationListAdapter(
        listener: TagViewHolder.Listener
) : BaseDiffUtilAdapter<TagRecommendationListAdapter.Model>() {

    init {
        delegatesManager
                .addDelegate(TagAdapterDelegate(listener))
                .addDelegate(TagPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        if(oldItem is Model.Tag && newItem is Model.Tag)
            return oldItem.data.tag == newItem.data.tag
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed class Model {
        data class Tag(val data: PlayTagUiModel): Model()
        object Placeholder: Model()
    }
}