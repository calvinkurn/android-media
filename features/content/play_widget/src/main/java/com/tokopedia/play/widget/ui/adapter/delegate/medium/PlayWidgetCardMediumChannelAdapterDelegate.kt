package com.tokopedia.play.widget.ui.adapter.delegate.medium

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumChannelAdapterDelegate(
        private val mediumCardChannelListener: PlayWidgetCardMediumChannelViewHolder.Listener
) : BaseAdapterDelegate<PlayWidgetMediumChannelUiModel, PlayWidgetMediumItemUiModel, PlayWidgetCardMediumChannelViewHolder>(
        PlayWidgetCardMediumChannelViewHolder.layoutRes
) {
    private val allowedTypes = listOf(
            PlayWidgetChannelType.Live,
            PlayWidgetChannelType.Vod,
            PlayWidgetChannelType.Upcoming,
            PlayWidgetChannelType.Unknown,
            PlayWidgetChannelType.Deleting
    )

    override fun onBindViewHolder(item: PlayWidgetMediumChannelUiModel, holder: PlayWidgetCardMediumChannelViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardMediumChannelViewHolder {
        return PlayWidgetCardMediumChannelViewHolder(basicView, mediumCardChannelListener)
    }

    override fun isForViewType(itemList: List<PlayWidgetMediumItemUiModel>, position: Int, isFlexibleType: Boolean): Boolean {
        val item = itemList[position]
        return if (item is PlayWidgetMediumChannelUiModel) item.channelType in allowedTypes
        else false
    }
}