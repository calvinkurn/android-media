package com.tokopedia.product.edit.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.adapter.viewholder.EmptyVideoViewHolder
import com.tokopedia.product.edit.adapter.viewholder.TitleVideoChoosenViewHolder
import com.tokopedia.product.edit.adapter.viewholder.SectionVideoRecommendationViewHolder
import com.tokopedia.product.edit.adapter.viewholder.VideoViewHolder
import com.tokopedia.product.edit.viewmodel.EmptyVideoViewModel
import com.tokopedia.product.edit.viewmodel.TitleVideoChoosenViewModel
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel
import com.tokopedia.product.edit.viewmodel.VideoViewModel

class ProductAddVideoAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(videoRecommendationViewModel: VideoRecommendationViewModel): Int {
        return SectionVideoRecommendationViewHolder.LAYOUT
    }

    fun type(videoViewModel: VideoViewModel): Int {
        return VideoViewHolder.LAYOUT
    }

    fun type(emptyVideoViewModel: EmptyVideoViewModel): Int {
        return EmptyVideoViewHolder.LAYOUT
    }

    fun type(titleVideoChoosenViewModel: TitleVideoChoosenViewModel): Int {
        return TitleVideoChoosenViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TitleVideoChoosenViewHolder.LAYOUT -> TitleVideoChoosenViewHolder(parent)
            VideoViewHolder.LAYOUT -> VideoViewHolder(parent)
            EmptyVideoViewHolder.LAYOUT -> EmptyVideoViewHolder(parent)
            SectionVideoRecommendationViewHolder.LAYOUT -> SectionVideoRecommendationViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}