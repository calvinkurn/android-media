package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.viewholder.TagAddedViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.TagAddedAdapterDelegate

/**
 * Created by jegul on 18/02/21
 */
class TagAddedListAdapter(
        listener: TagAddedViewHolder.Listener
) : BaseDiffUtilAdapter<String>() {

    init {
        delegatesManager
                .addDelegate(TagAddedAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}