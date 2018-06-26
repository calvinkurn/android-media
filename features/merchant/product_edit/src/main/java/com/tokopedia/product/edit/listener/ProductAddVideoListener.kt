package com.tokopedia.product.edit.listener

import com.tokopedia.product.edit.model.VideoRecommendationData

interface ProductAddVideoListener {

    fun onSuccessGetVideoRecommendationFeatured(videoRecommendationDataList: List<VideoRecommendationData>)

}