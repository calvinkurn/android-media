package com.tokopedia.product.manage.item.video.view.adapter

import com.tokopedia.product.manage.item.video.view.model.TitleVideoRecommendationViewModel
import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel

interface ProductAddVideoRecommendationTypeFactory {

    fun type(titleVideoRecommendationViewModel: TitleVideoRecommendationViewModel): Int

    fun type(videoRecommendationViewModel: VideoRecommendationViewModel): Int

}