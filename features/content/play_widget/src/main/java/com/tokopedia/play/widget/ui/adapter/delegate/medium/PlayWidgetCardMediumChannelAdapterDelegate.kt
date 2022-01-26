package com.tokopedia.play.widget.ui.adapter.delegate.medium

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumChannelAdapterDelegate(
        private val mediumCardChannelListener: PlayWidgetCardMediumChannelViewHolder.Listener
) : BaseAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetCardMediumChannelViewHolder>(
        PlayWidgetCardMediumChannelViewHolder.layoutRes
) {
    private val allowedTypes = listOf(
            PlayWidgetChannelType.Live,
            PlayWidgetChannelType.Vod,
            PlayWidgetChannelType.Upcoming,
            PlayWidgetChannelType.Unknown,
            PlayWidgetChannelType.Deleting
    )

    override fun onBindViewHolder(item: PlayWidgetChannelUiModel, holder: PlayWidgetCardMediumChannelViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardMediumChannelViewHolder {
        return PlayWidgetCardMediumChannelViewHolder(basicView, mediumCardChannelListener)
    }

    override fun isForViewType(itemList: List<PlayWidgetItemUiModel>, position: Int, isFlexibleType: Boolean): Boolean {
        val item = itemList[position]
        return if (item is PlayWidgetChannelUiModel) item.channelType in allowedTypes
        else false
    }
}