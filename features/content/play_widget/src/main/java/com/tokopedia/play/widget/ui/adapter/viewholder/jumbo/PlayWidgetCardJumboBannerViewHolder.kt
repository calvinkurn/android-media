package com.tokopedia.play.widget.ui.adapter.viewholder.jumbo

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboBannerUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboBannerViewHolder(
    itemView: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private var background: AppCompatImageView = itemView.findViewById(R.id.play_widget_banner)

    fun bind(item: PlayWidgetJumboBannerUiModel) {
        background.loadImage(item.imageUrl)
        itemView.setOnClickListener {
            listener.onBannerClicked(it, item, adapterPosition)
            RouteManager.route(it.context, item.appLink)
        }
    }

    companion object {
        @LayoutRes
        val layoutRes = R.layout.item_play_widget_card_banner_jumbo
    }

    interface Listener {

        fun onBannerClicked(
            view: View,
            item: PlayWidgetJumboBannerUiModel,
            position: Int
        )
    }
}