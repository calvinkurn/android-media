package com.tokopedia.play.widget.ui.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetCardType
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumBannerViewHolder(itemView: View) : PlayWidgetCardMediumViewHolder(itemView) {

    private var background: AppCompatImageView = itemView.findViewById(R.id.play_widget_banner)

    override fun bind(item: PlayWidgetCardUiModel) {
        if (item.type == PlayWidgetCardType.Banner) return
        background.loadImage(item.card.backgroundUrl)
        // TODO add on click listener
    }
}