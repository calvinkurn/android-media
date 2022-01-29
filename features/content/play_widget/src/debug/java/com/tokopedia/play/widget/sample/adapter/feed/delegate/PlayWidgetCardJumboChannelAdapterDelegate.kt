package com.tokopedia.play.widget.sample.adapter.feed.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.play.widget.sample.adapter.feed.viewholder.PlayWidgetCardJumboViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboChannelAdapterDelegate(
    private val listener: PlayWidgetCardJumboViewHolder.Listener
) : BaseAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetCardJumboViewHolder>(
    PlayWidgetCardJumboViewHolder.layoutRes
) {
    private val allowedTypes = listOf(
        PlayWidgetChannelType.Live,
        PlayWidgetChannelType.Vod,
        PlayWidgetChannelType.Upcoming,
        PlayWidgetChannelType.Unknown
    )

    override fun onBindViewHolder(
        item: PlayWidgetChannelUiModel,
        holder: PlayWidgetCardJumboViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayWidgetCardJumboViewHolder {
        return PlayWidgetCardJumboViewHolder(basicView, listener)
    }

    override fun isForViewType(
        itemList: List<PlayWidgetItemUiModel>,
        position: Int,
        isFlexibleType: Boolean
    ): Boolean {
        val item = itemList[position]
        return if (item is PlayWidgetChannelUiModel) item.channelType in allowedTypes
        else false
    }
}