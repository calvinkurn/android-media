package com.tokopedia.product.edit.mapper

import com.tokopedia.product.edit.model.VideoRecommendationData
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel
import java.util.ArrayList

class ProductAddVideoRecommendationMapper {

    fun transformDataToVideoViewModel(videoRecommendationDataList: List<VideoRecommendationData>): List<VideoRecommendationViewModel> {
        val videoRecommendationViewModelList = ArrayList<VideoRecommendationViewModel>()
        for (videoRecommendationData in videoRecommendationDataList) {
            val videoRecommendationViewModel = VideoRecommendationViewModel()
            videoRecommendationViewModel.title = videoRecommendationData.title
            videoRecommendationViewModel.id = videoRecommendationData.id
            videoRecommendationViewModelList.add(videoRecommendationViewModel)
        }
        return videoRecommendationViewModelList
    }
}