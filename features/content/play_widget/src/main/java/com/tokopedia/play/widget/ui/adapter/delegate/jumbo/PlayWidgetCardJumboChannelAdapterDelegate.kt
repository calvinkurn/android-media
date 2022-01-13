package com.tokopedia.play.widget.ui.adapter.delegate.jumbo

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboChannelAdapterDelegate(
    private val jumboCardChannelListener: PlayWidgetCardJumboChannelViewHolder.Listener
) : BaseAdapterDelegate<PlayWidgetJumboChannelUiModel, PlayWidgetJumboItemUiModel, PlayWidgetCardJumboChannelViewHolder>(
    PlayWidgetCardJumboChannelViewHolder.layoutRes
) {
    private val allowedTypes = listOf(
        PlayWidgetChannelType.Live,
        PlayWidgetChannelType.Vod,
        PlayWidgetChannelType.Upcoming,
        PlayWidgetChannelType.Unknown,
        PlayWidgetChannelType.Deleting
    )

    override fun onBindViewHolder(
        item: PlayWidgetJumboChannelUiModel,
        holder: PlayWidgetCardJumboChannelViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayWidgetCardJumboChannelViewHolder {
        return PlayWidgetCardJumboChannelViewHolder(basicView, jumboCardChannelListener)
    }

    override fun isForViewType(
        itemList: List<PlayWidgetJumboItemUiModel>,
        position: Int,
        isFlexibleType: Boolean
    ): Boolean {
        val item = itemList[position]
        return if (item is PlayWidgetJumboChannelUiModel) item.channelType in allowedTypes
        else false
    }
}