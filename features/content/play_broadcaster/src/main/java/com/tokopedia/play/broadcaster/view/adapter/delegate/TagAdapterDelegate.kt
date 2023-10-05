package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.viewholder.TagViewHolder
import com.tokopedia.play_common.R as commonR

/**
 * Created by jegul on 18/02/21
 */
internal class TagAdapterDelegate private constructor() {

    class Tag(
        private val listener: TagViewHolder.Tag.Listener
    ) : TypedAdapterDelegate<PlayTagItem, Any, TagViewHolder.Tag>(commonR.layout.view_play_empty) {
        override fun onBindViewHolder(
            item: PlayTagItem,
            holder: TagViewHolder.Tag
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TagViewHolder.Tag {
            return TagViewHolder.Tag.create(parent, listener)
        }
    }

    class Placeholder : TypedAdapterDelegate<Unit, Any, TagViewHolder.Placeholder>(commonR.layout.view_play_empty) {
        override fun onBindViewHolder(item: Unit, holder: TagViewHolder.Placeholder) { }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): TagViewHolder.Placeholder {
            return TagViewHolder.Placeholder.create(parent)
        }
    }
}
