package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumBannerUiModel
import com.tokopedia.play_common.view.loadImage


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumBannerViewHolder(
        itemView: View,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private var background: AppCompatImageView = itemView.findViewById(R.id.play_widget_banner)

    fun bind(item: PlayWidgetMediumBannerUiModel) {
        background.loadImage(item.imageUrl)
        itemView.setOnClickListener {
            listener.onBannerClicked(it, item, adapterPosition)
            RouteManager.route(it.context, item.appLink)
        }
    }

    companion object {
        @LayoutRes val layoutRes = R.layout.item_play_widget_card_banner_medium
    }

    interface Listener {

        fun onBannerClicked(
                view: View,
                item: PlayWidgetMediumBannerUiModel,
                position: Int
        )
    }
}