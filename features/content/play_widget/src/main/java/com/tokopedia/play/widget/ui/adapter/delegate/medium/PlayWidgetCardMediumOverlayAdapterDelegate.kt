package com.tokopedia.play.widget.ui.adapter.delegate.medium

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumOverlayViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumOverlayAdapterDelegate(
        private val mediumCardOverlayListener: PlayWidgetCardMediumOverlayViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetBackgroundUiModel, PlayWidgetBackgroundUiModel, PlayWidgetCardMediumOverlayViewHolder>(
        PlayWidgetCardMediumOverlayViewHolder.layoutRes
) {

    override fun onBindViewHolder(item: PlayWidgetBackgroundUiModel, holder: PlayWidgetCardMediumOverlayViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardMediumOverlayViewHolder {
        return PlayWidgetCardMediumOverlayViewHolder(basicView, mediumCardOverlayListener)
    }
}