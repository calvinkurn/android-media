package com.tokopedia.play.widget.ui.adapter.delegate.small

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetCardSmallChannelAdapterDelegate(
        private val smallCardChannelListener: PlayWidgetCardSmallChannelViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetCardSmallChannelViewHolder>(
        PlayWidgetCardSmallChannelViewHolder.layout
) {

    override fun onBindViewHolder(item: PlayWidgetChannelUiModel, holder: PlayWidgetCardSmallChannelViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardSmallChannelViewHolder {
        return PlayWidgetCardSmallChannelViewHolder(basicView, smallCardChannelListener)
    }
}