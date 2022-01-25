package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumOverlayViewHolder(
        itemView: View,
        val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: PlayWidgetBackgroundUiModel) {
//        itemView.addOnImpressionListener(item.impressHolder) {
//            listener.onOverlayImpressed(itemView, item, adapterPosition)
//        }
        itemView.setOnClickListener {
            listener.onOverlayClicked(it, item, adapterPosition)
            RouteManager.route(it.context, item.overlayImageAppLink)
        }
    }

    companion object {
        @LayoutRes val layoutRes = R.layout.item_play_widget_card_overlay_medium
    }

    interface Listener {

        fun onOverlayImpressed(
                view: View,
                item: PlayWidgetBackgroundUiModel,
                position: Int
        )

        fun onOverlayClicked(
                view: View,
                item: PlayWidgetBackgroundUiModel,
                position: Int
        )
    }
}