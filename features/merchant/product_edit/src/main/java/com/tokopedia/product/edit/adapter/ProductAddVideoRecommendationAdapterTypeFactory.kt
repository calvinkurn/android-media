package com.tokopedia.product.edit.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.adapter.viewholder.*
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

class ProductAddVideoRecommendationAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(videoRecommendationViewModel: VideoRecommendationViewModel): Int {
        return VideoRecommendationViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            VideoRecommendationViewHolder.LAYOUT -> VideoRecommendationViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}