package com.tokopedia.play_common.widget.playBannerCarousel.viewHolder

import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage
import com.tokopedia.play_common.widget.playBannerCarousel.extension.showOrHideView
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import kotlinx.android.synthetic.main.item_play_banner_carousel.view.*
import kotlinx.android.synthetic.main.layout_viewer_badge.view.*


class PlayBannerCarouselItemViewHolder (private val parent: View): BasePlayBannerCarouselViewHolder<PlayBannerCarouselItemDataModel>(parent){

    val playerView: PlayerView = parent.player_view
    val thumbnail: ImageView = parent.thumbnail
    override fun bind(dataModel: PlayBannerCarouselItemDataModel, listener: PlayBannerCarouselViewEventListener?) {
        parent.tag = this
        itemView.setOnClickListener { listener?.onItemClick(dataModel, adapterPosition) }
        itemView.addOnImpressionListener(dataModel){ listener?.onItemImpress(dataModel, adapterPosition) }
        parent.thumbnail?.loadImage(dataModel.coverUrl)
        parent.channel_title?.text = dataModel.channelTitle
        parent.channel_name?.text = dataModel.channelCreator
        parent.viewer?.text = dataModel.countView
        parent.promo_badge?.setOnClickListener { listener?.onPromoBadgeClick(dataModel, adapterPosition) }
        parent.promo_badge?.showOrHideView(dataModel.promoUrl.isNotBlank())
        parent.live_badge?.showOrHideView(dataModel.isLive)
        parent.viewer_badge?.showOrHideView(dataModel.isShowTotalView)
    }
}