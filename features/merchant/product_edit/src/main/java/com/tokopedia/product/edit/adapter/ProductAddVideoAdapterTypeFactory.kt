package com.tokopedia.product.edit.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.adapter.viewholder.EmptyVideoViewHolder
import com.tokopedia.product.edit.adapter.viewholder.TitleVideoChosenViewHolder
import com.tokopedia.product.edit.adapter.viewholder.SectionVideoRecommendationViewHolder
import com.tokopedia.product.edit.adapter.viewholder.VideoViewHolder
import com.tokopedia.product.edit.listener.SectionVideoRecommendationListener
import com.tokopedia.product.edit.listener.VideoChosenListener
import com.tokopedia.product.edit.viewmodel.*

class ProductAddVideoAdapterTypeFactory(var sectionVideoRecommendationListener: SectionVideoRecommendationListener,
                                        var videoChoosenListener: VideoChosenListener) : BaseAdapterTypeFactory() {

    fun type(sectionVideoRecommendationViewModel: SectionVideoRecommendationViewModel): Int {
        return SectionVideoRecommendationViewHolder.LAYOUT
    }

    fun type(videoViewModel: VideoViewModel): Int {
        return VideoViewHolder.LAYOUT
    }

    fun type(emptyVideoViewModel: EmptyVideoViewModel): Int {
        return EmptyVideoViewHolder.LAYOUT
    }

    fun type(titleVideoChoosenViewModel: TitleVideoChosenViewModel): Int {
        return TitleVideoChosenViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TitleVideoChosenViewHolder.LAYOUT -> TitleVideoChosenViewHolder(parent)
            VideoViewHolder.LAYOUT -> VideoViewHolder(parent, videoChoosenListener)
            EmptyVideoViewHolder.LAYOUT -> EmptyVideoViewHolder(parent)
            SectionVideoRecommendationViewHolder.LAYOUT -> SectionVideoRecommendationViewHolder(parent, sectionVideoRecommendationListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}