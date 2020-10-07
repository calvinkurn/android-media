package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel


/**
 * Created by mzennis on 05/10/20.
 */
abstract class PlayWidgetCardMediumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: PlayWidgetItemUiModel)

}