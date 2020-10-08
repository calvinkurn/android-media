package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetMediumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val widgetMediumView = itemView as PlayWidgetMediumView

    fun bind(item: PlayWidgetUiModel.Medium) {
        widgetMediumView.setData(item)
    }

    companion object {
        val layout = R.layout.item_play_widget_medium
    }
}