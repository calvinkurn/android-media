package com.tokopedia.product.edit.mapper

import com.tokopedia.product.edit.model.VideoRecommendationData
import com.tokopedia.product.edit.viewmodel.VideoViewModel
import java.util.ArrayList

class ProductAddVideoMapper {

    fun transformDataToVideoViewModel(videoIDs: List<String>): List<VideoViewModel> {
        val videoViewModelList = ArrayList<VideoViewModel>()
        for (videoID in videoIDs) {
            val videoViewModel = VideoViewModel()
            videoViewModel.videoID = videoID
            videoViewModelList.add(videoViewModel)
        }
        return videoViewModelList
    }
}