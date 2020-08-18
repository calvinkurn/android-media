package com.tokopedia.play_common.widget.playBannerCarousel.viewHolder

import android.view.View
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import kotlinx.android.synthetic.main.item_play_banner_carousel_banner.view.*

class PlayBannerCarouselBannerViewHolder(parent: View): BasePlayBannerCarouselViewHolder<PlayBannerCarouselBannerDataModel>(parent){
    override fun bind(dataModel: PlayBannerCarouselBannerDataModel, listener: PlayBannerCarouselViewEventListener?) {
        itemView.background_banner_mix_more?.loadImage(dataModel.imageUrl)
        itemView.setOnClickListener { listener?.onSeeMoreBannerClick(dataModel, adapterPosition) }
    }
}