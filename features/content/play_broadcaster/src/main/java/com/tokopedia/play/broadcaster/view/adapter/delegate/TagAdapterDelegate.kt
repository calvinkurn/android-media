package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.databinding.ItemTagBinding
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagViewHolder

/**
 * Created by jegul on 18/02/21
 */
class TagAdapterDelegate(
        private val listener: TagViewHolder.Listener
) : TypedAdapterDelegate<PlayTagUiModel, PlayTagUiModel, TagViewHolder>(TagViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayTagUiModel, holder: TagViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TagViewHolder {
        return TagViewHolder(ItemTagBinding.bind(basicView), listener)
    }
}