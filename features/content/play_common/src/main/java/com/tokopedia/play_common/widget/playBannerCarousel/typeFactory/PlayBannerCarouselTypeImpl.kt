package com.tokopedia.play_common.widget.playBannerCarousel.typeFactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play_common.R
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselAddStoryDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselEmptyDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselSeeMoreDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.viewHolder.*

class PlayBannerCarouselTypeImpl(
        private var listener: PlayBannerCarouselViewEventListener? = null
) : PlayBannerCarouselTypeFactory{
    companion object{
        private val ADD_STORY = R.layout.item_play_banner_create_story
        private val SEE_MORE = R.layout.item_play_banner_carousel_see_more
        private val EMPTY = R.layout.item_play_banner_carousel_empty
        private val CONTENT_ITEM = R.layout.item_play_banner_carousel
    }

    override fun type(dataModel: PlayBannerCarouselEmptyDataModel): Int {
        return EMPTY
    }

    override fun type(dataModel: PlayBannerCarouselSeeMoreDataModel): Int {
        return SEE_MORE
    }

    override fun type(dataModel: PlayBannerCarouselItemDataModel): Int {
        return CONTENT_ITEM
    }

    override fun type(dataModel: PlayBannerCarouselAddStoryDataModel): Int {
        return ADD_STORY
    }

    override fun createViewHolder(view: View, viewType: Int): BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel> {
        return when (viewType) {
            CONTENT_ITEM -> {
                PlayBannerCarouselItemViewHolder(view, listener)
            }
            SEE_MORE -> {
                PlayBannerCarouselSeeMoreViewHolder(view, listener)
            }
            EMPTY -> {
                PlayBannerCarouselEmptyViewHolder(view)
            }
            ADD_STORY -> {
                PlayBannerCarouselAddStoryViewHolder(view)
            }
            else -> {
                throw Exception("Layout not supported")
            }
        } as BasePlayBannerCarouselViewHolder<BasePlayBannerCarouselModel>
    }
}