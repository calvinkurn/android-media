package com.tokopedia.play.widget.ui.adapter.viewholder.small

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.custom.PlayWidgetCardChannelSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel

/**
 * Created by jegul on 06/10/20
 */
class PlayWidgetCardSmallChannelViewHolder(
        itemView: View,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetCardSmallView: PlayWidgetCardChannelSmallView = itemView as PlayWidgetCardChannelSmallView

    private val widgetCardSmallListener = object : PlayWidgetCardChannelSmallView.Listener {

        override fun onChannelClicked(view: PlayWidgetCardChannelSmallView, model: PlayWidgetSmallChannelUiModel) {
            listener.onChannelClicked(view, model, adapterPosition)
        }
    }

    init {
        playWidgetCardSmallView.setListener(widgetCardSmallListener)
    }

    fun bind(item: PlayWidgetSmallChannelUiModel) {
        playWidgetCardSmallView.setModel(item)
    }

    companion object {
        val layout = R.layout.item_play_widget_card_channel_small
    }

    interface Listener {

        fun onChannelImpressed(
                view: View,
                item: PlayWidgetSmallChannelUiModel,
                position: Int
        )

        fun onChannelClicked(
                view: View,
                item: PlayWidgetSmallChannelUiModel,
                position: Int
        )
    }
}