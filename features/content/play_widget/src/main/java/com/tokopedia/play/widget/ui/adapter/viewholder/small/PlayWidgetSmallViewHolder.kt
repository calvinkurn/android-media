package com.tokopedia.play.widget.ui.adapter.viewholder.small

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSmallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val widgetSmallView = itemView as PlayWidgetSmallView

    fun bind(item: PlayWidgetUiModel.Small) {
        widgetSmallView.setData(item)
    }

    companion object {
        val layout = R.layout.item_play_widget_small
    }
}