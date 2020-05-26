package com.tokopedia.play_common.widget.playBannerCarousel.typeFactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play_common.R
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselEmptyDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselSeeMoreDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.BasePlayBannerCarouselViewHolder
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselEmptyViewHolder
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselItemViewHolder
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.PlayBannerCarouselSeeMoreViewHolder

class PlayBannerCarouselTypeImpl(
        private var listener: PlayBannerCarouselViewEventListener? = null
) : PlayBannerCarouselTypeFactory{
    override fun type(dataModel: PlayBannerCarouselEmptyDataModel): Int {
        return R.layout.item_play_banner_carousel_empty
    }

    override fun type(dataModel: PlayBannerCarouselSeeMoreDataModel): Int {
        return R.layout.item_play_banner_carousel_see_more
    }

    override fun type(dataModel: PlayBannerCarouselItemDataModel): Int {
        return R.layout.item_play_banner_carousel
    }

    override fun createViewHolder(view: View, viewType: Int): BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel> {
        return when (viewType) {
            R.layout.item_play_banner_carousel -> {
                PlayBannerCarouselItemViewHolder(view, listener)
            }
            R.layout.item_play_banner_carousel_see_more -> {
                PlayBannerCarouselSeeMoreViewHolder(view, listener)
            }
            R.layout.item_play_banner_carousel_empty -> {
                PlayBannerCarouselEmptyViewHolder(view)
            }
            else -> {
                throw Exception("Layout not supported")
            }
        } as BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel>
    }
}