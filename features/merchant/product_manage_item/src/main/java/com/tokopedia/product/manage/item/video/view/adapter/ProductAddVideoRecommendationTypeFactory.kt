package com.tokopedia.product.manage.item.view.adapter

import com.tokopedia.product.manage.item.view.viewmodel.*

interface ProductAddVideoRecommendationTypeFactory {

    fun type(titleVideoRecommendationViewModel: TitleVideoRecommendationViewModel): Int

    fun type(videoRecommendationViewModel: VideoRecommendationViewModel): Int

}