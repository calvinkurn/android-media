package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumBannerUiModel
import com.tokopedia.play.widget.R
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumBannerViewHolder(itemView: View) : PlayWidgetCardMediumViewHolder(itemView) {

    private var background: AppCompatImageView = itemView.findViewById(R.id.play_widget_banner)

    override fun bind(item: PlayWidgetItemUiModel) {
        if (item !is PlayWidgetMediumBannerUiModel) return
        background.loadImage(item.imageUrl)
        // TODO add on click listener
    }
}