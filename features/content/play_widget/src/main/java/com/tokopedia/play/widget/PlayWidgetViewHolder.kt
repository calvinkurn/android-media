package com.tokopedia.play.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetViewHolder(
        itemView: View,
        private val widgetListener: PlayWidgetListener? = null
) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetView = itemView as PlayWidgetView

    fun bind(item: PlayWidgetUiModel, isFromAutoRefresh: Boolean = false) {
        playWidgetView.setListener(widgetListener)
        playWidgetView.setModel(item, isFromAutoRefresh)
    }

    companion object {
        val layout = R.layout.item_play_widget
    }
}