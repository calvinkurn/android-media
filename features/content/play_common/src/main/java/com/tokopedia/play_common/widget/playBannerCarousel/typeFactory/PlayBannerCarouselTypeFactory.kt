package com.tokopedia.play_common.widget.playBannerCarousel.typeFactory

import android.view.View
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.BasePlayBannerCarouselViewHolder

interface PlayBannerCarouselTypeFactory {
    fun type(dataModel: PlayBannerCarouselOverlayImageDataModel) : Int
    fun type(dataModel: PlayBannerCarouselBannerDataModel) : Int
    fun type(dataModel: PlayBannerCarouselItemDataModel) : Int

    fun createViewHolder(view: View, viewType: Int): BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel>
}