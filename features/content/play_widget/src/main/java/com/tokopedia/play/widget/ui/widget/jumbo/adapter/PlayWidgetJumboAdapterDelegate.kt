package com.tokopedia.play.widget.ui.widget.jumbo.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetJumboAdapterDelegate {

    internal class Channel(
        private val cardChannelListener: PlayWidgetJumboViewHolder.Channel.Listener,
    ) : TypedAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetJumboViewHolder.Channel>(
        R.layout.item_play_widget_card_jumbo_channel
    ) {

        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: PlayWidgetJumboViewHolder.Channel
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetJumboViewHolder.Channel {
            return PlayWidgetJumboViewHolder.Channel.create(basicView, cardChannelListener)
        }
    }
}