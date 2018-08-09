package com.tokopedia.product.manage.item.video.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.item.video.view.adapter.viewholder.TitleVideoRecommendationViewHolder
import com.tokopedia.product.manage.item.video.view.adapter.viewholder.VideoRecommendationViewHolder
import com.tokopedia.product.manage.item.video.view.listener.VideoRecommendationListener
import com.tokopedia.product.manage.item.video.view.model.TitleVideoRecommendationViewModel
import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel

class ProductAddVideoRecommendationAdapterTypeFactory(private var videoRecommendationListener: VideoRecommendationListener) : BaseAdapterTypeFactory(), ProductAddVideoRecommendationTypeFactory {

    override fun type(titleVideoRecommendationViewModel: TitleVideoRecommendationViewModel): Int {
        return TitleVideoRecommendationViewHolder.LAYOUT
    }

    override fun type(videoRecommendationViewModel: VideoRecommendationViewModel): Int {
        return VideoRecommendationViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TitleVideoRecommendationViewHolder.LAYOUT -> TitleVideoRecommendationViewHolder(parent)
            VideoRecommendationViewHolder.LAYOUT -> VideoRecommendationViewHolder(parent, videoRecommendationListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}