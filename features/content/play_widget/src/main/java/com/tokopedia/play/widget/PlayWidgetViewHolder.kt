package com.tokopedia.play.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.contract.IPlayWidgetView


/**
 * Created by mzennis on 05/10/20.
 */
open class PlayWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetView = itemView as IPlayWidgetView

    fun bind(item: PlayWidgetUiModel) {
        playWidgetView.setData(item)
    }
}