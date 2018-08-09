package com.tokopedia.product.manage.item.video.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.item.video.view.adapter.viewholder.EmptyVideoViewHolder
import com.tokopedia.product.manage.item.video.view.adapter.viewholder.SectionVideoRecommendationViewHolder
import com.tokopedia.product.manage.item.video.view.adapter.viewholder.TitleVideoChosenViewHolder
import com.tokopedia.product.manage.item.video.view.adapter.viewholder.VideoViewHolder
import com.tokopedia.product.manage.item.video.view.listener.SectionVideoRecommendationListener
import com.tokopedia.product.manage.item.video.view.listener.VideoChosenListener
import com.tokopedia.product.manage.item.video.view.model.EmptyVideoViewModel
import com.tokopedia.product.manage.item.video.view.model.SectionVideoRecommendationViewModel
import com.tokopedia.product.manage.item.video.view.model.TitleVideoChosenViewModel
import com.tokopedia.product.manage.item.video.view.model.VideoViewModel

class ProductAddVideoAdapterTypeFactory(private var sectionVideoRecommendationListener: SectionVideoRecommendationListener,
                                        private var videoChoosenListener: VideoChosenListener) : BaseAdapterTypeFactory(), ProductAddVideoTypeFactory {

    override fun type(sectionVideoRecommendationViewModel: SectionVideoRecommendationViewModel): Int {
        return SectionVideoRecommendationViewHolder.LAYOUT
    }

    override fun type(videoViewModel: VideoViewModel): Int {
        return VideoViewHolder.LAYOUT
    }

    override fun type(emptyVideoViewModel: EmptyVideoViewModel): Int {
        return EmptyVideoViewHolder.LAYOUT
    }

    override fun type(titleVideoChoosenViewModel: TitleVideoChosenViewModel): Int {
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