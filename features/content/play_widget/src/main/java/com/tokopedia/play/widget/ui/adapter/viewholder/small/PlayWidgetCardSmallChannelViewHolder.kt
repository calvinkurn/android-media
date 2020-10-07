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
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetCardSmallView: PlayWidgetCardChannelSmallView = itemView as PlayWidgetCardChannelSmallView

    fun bind(item: PlayWidgetSmallChannelUiModel) {
        playWidgetCardSmallView.setModel(item)
    }

    companion object {
        val layout = R.layout.item_play_widget_card_channel_small
    }
}