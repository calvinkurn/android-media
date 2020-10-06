package com.tokopedia.play.widget.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.custom.PlayWidgetCardSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel

/**
 * Created by jegul on 06/10/20
 */
class PlayWidgetCardSmallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetCardSmallView: PlayWidgetCardSmallView = itemView.findViewById(R.id.play_widget_card_small)

    fun bind(item: PlayWidgetCardUiModel) {
        playWidgetCardSmallView.setModel(item)
    }

    companion object {
        val LAYOUT = R.layout.item_play_widget_card_small
    }
}