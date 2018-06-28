package com.tokopedia.product.edit.listener

import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

interface ProductAddVideoListener {

    fun onSuccessGetVideoRecommendationFeatured(videoRecommendationFeaturedList : List<VideoRecommendationViewModel>)

}