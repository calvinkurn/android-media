package com.tokopedia.play.widget.ui.adapter.delegate.small

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallItemUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetCardSmallChannelAdapterDelegate(
        private val smallCardChannelListener: PlayWidgetCardSmallChannelViewHolder.Listener
) : TypedAdapterDelegate<PlayWidgetSmallChannelUiModel, PlayWidgetSmallItemUiModel, PlayWidgetCardSmallChannelViewHolder>(
        PlayWidgetCardSmallChannelViewHolder.layout
) {

    override fun onBindViewHolder(item: PlayWidgetSmallChannelUiModel, holder: PlayWidgetCardSmallChannelViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardSmallChannelViewHolder {
        return PlayWidgetCardSmallChannelViewHolder(basicView, smallCardChannelListener)
    }
}