package com.tokopedia.play.widget.ui.adapter.delegate.medium

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumItemUiModel


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumChannelAdapterDelegate(
        private val listener: PlayWidgetCardMediumChannelViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetMediumChannelUiModel, PlayWidgetMediumItemUiModel, PlayWidgetCardMediumChannelViewHolder>(
        PlayWidgetCardMediumChannelViewHolder.layoutRes
) {

    override fun onBindViewHolder(item: PlayWidgetMediumChannelUiModel, holder: PlayWidgetCardMediumChannelViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardMediumChannelViewHolder {
        return PlayWidgetCardMediumChannelViewHolder(basicView, listener)
    }

    override fun onBindViewHolderWithPayloads(item: PlayWidgetMediumChannelUiModel, holder: PlayWidgetCardMediumChannelViewHolder, payloads: Bundle) {
        if (payloads.isEmpty) return
        if (payloads.containsKey(KEY_CHANNEL_REMINDER)) {
            holder.revertToOriginalReminderState()
        }
    }

    companion object {
        const val KEY_CHANNEL_REMINDER = "channel_reminder"
    }
}