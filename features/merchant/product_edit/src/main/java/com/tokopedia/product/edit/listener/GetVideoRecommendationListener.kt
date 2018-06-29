package com.tokopedia.product.edit.listener

import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

interface GetVideoRecommendationListener {

    fun onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList : List<VideoRecommendationViewModel>)

}