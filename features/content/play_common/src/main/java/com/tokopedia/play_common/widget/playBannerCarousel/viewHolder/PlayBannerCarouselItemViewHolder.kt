package com.tokopedia.play_common.widget.playBannerCarousel.viewHolder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.extension.showOrHideView
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import kotlinx.android.synthetic.main.item_play_banner_carousel.view.*
import kotlinx.android.synthetic.main.layout_viewer_badge.view.*


class PlayBannerCarouselItemViewHolder (private val parent: View, private val listener: PlayBannerCarouselViewEventListener?): BasePlayBannerCarouselViewHolder<PlayBannerCarouselItemDataModel>(parent){
    val mediaContainer = parent.media_container
    val progressBar = parent.progress_bar
    val playerView = parent.player_view
    override fun bind(dataModel: PlayBannerCarouselItemDataModel) {
        parent.tag = this
        itemView.setOnClickListener { listener?.onItemClick(dataModel, adapterPosition) }
        itemView.addOnImpressionListener(dataModel){ listener?.onItemImpress(dataModel, adapterPosition) }
        parent.channel_title?.text = dataModel.channelTitle
        parent.channel_name?.text = dataModel.channelCreator
        parent.viewer?.text = dataModel.countView
        parent.promo_badge?.setOnClickListener { listener?.onPromoBadgeClick(dataModel, adapterPosition) }
        parent.promo_badge?.showOrHideView(dataModel.promoUrl.isNotBlank())
        parent.live_badge?.showOrHideView(dataModel.isLive)
        parent.viewer_badge?.showOrHideView(dataModel.isShowTotalView)
    }
}