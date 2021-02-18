package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.viewholder.TagAddedViewHolder

/**
 * Created by jegul on 18/02/21
 */
class TagAddedAdapterDelegate(
        private val listener: TagAddedViewHolder.Listener
) : TypedAdapterDelegate<String, String, TagAddedViewHolder>(TagAddedViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: String, holder: TagAddedViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TagAddedViewHolder {
        return TagAddedViewHolder(basicView, listener)
    }
}