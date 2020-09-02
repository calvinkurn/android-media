package com.tokopedia.play_common.widget.playBannerCarousel.typeFactory

import android.view.View
import com.tokopedia.play_common.R
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.BasePlayBannerCarouselViewHolder
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselBannerViewHolder
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselItemViewHolder
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselOverlayImageViewHolder

class PlayBannerCarouselTypeImpl : PlayBannerCarouselTypeFactory{
    companion object{
        private val BANNER = R.layout.item_play_banner_carousel_banner
        private val EMPTY = R.layout.item_play_banner_carousel_empty
        private val CONTENT_ITEM = R.layout.item_play_banner_carousel
    }

    override fun type(dataModel: PlayBannerCarouselOverlayImageDataModel): Int {
        return EMPTY
    }

    override fun type(dataModel: PlayBannerCarouselBannerDataModel): Int {
        return BANNER
    }

    override fun type(dataModel: PlayBannerCarouselItemDataModel): Int {
        return CONTENT_ITEM
    }

    override fun createViewHolder(view: View, viewType: Int): BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel> {
        return when (viewType) {
            CONTENT_ITEM -> {
                PlayBannerCarouselItemViewHolder(view)
            }
            BANNER -> {
                PlayBannerCarouselBannerViewHolder(view)
            }
            EMPTY -> {
                PlayBannerCarouselOverlayImageViewHolder(view)
            }
            else -> {
                throw Exception("Layout not supported")
            }
        } as BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel>
    }
}