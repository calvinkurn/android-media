package com.tokopedia.play_common.widget.playBannerCarousel.typeFactory

import android.view.View
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselAddStoryDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselEmptyDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselSeeMoreDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.BasePlayBannerCarouselViewHolder

interface PlayBannerCarouselTypeFactory {
    fun type(dataModel: PlayBannerCarouselEmptyDataModel) : Int
    fun type(dataModel: PlayBannerCarouselSeeMoreDataModel) : Int
    fun type(dataModel: PlayBannerCarouselItemDataModel) : Int
    fun type(dataModel: PlayBannerCarouselAddStoryDataModel) : Int

    fun createViewHolder(view: View, viewType: Int): BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel>
}