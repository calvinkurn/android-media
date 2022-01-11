

package com.tokopedia.play.widget.ui.adapter.delegate.large

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeChannelAdapterDelegate(
    private val largeCardChannelListener: PlayWidgetCardLargeChannelViewHolder.Listener
) : BaseAdapterDelegate<PlayWidgetLargeChannelUiModel, PlayWidgetLargeItemUiModel, PlayWidgetCardLargeChannelViewHolder>(
    PlayWidgetCardLargeChannelViewHolder.layoutRes
) {
    private val allowedTypes = listOf(
        PlayWidgetChannelType.Live,
        PlayWidgetChannelType.Vod,
        PlayWidgetChannelType.Upcoming,
        PlayWidgetChannelType.Unknown,
        PlayWidgetChannelType.Deleting
    )

    override fun onBindViewHolder(
        item: PlayWidgetLargeChannelUiModel,
        holder: PlayWidgetCardLargeChannelViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayWidgetCardLargeChannelViewHolder {
        return PlayWidgetCardLargeChannelViewHolder(basicView, largeCardChannelListener)
    }

    override fun isForViewType(
        itemList: List<PlayWidgetLargeItemUiModel>,
        position: Int,
        isFlexibleType: Boolean
    ): Boolean {
        val item = itemList[position]
        return if (item is PlayWidgetLargeChannelUiModel) item.channelType in allowedTypes
        else false
    }
}