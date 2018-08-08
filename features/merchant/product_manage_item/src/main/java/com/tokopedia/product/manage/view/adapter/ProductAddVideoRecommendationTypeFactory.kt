package com.tokopedia.product.edit.view.adapter

import com.tokopedia.product.edit.view.viewmodel.*

interface ProductAddVideoRecommendationTypeFactory {

    fun type(titleVideoRecommendationViewModel: TitleVideoRecommendationViewModel): Int

    fun type(videoRecommendationViewModel: VideoRecommendationViewModel): Int

}