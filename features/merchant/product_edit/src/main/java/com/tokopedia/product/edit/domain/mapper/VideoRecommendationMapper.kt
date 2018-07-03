package com.tokopedia.product.edit.domain.mapper

import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel
import java.util.ArrayList

class VideoRecommendationMapper {

    fun transformDataToVideoViewModel(youtubeVideoModelList: List<YoutubeVideoModel>): List<VideoRecommendationViewModel> {
        val videoRecommendationViewModelList = ArrayList<VideoRecommendationViewModel>()
        for (youtubeVideoModel in youtubeVideoModelList) {
            val videoRecommendationViewModel = VideoRecommendationViewModel()
            if(!youtubeVideoModel.items?.isEmpty()!!){
                videoRecommendationViewModel.videoID = youtubeVideoModel.id
                videoRecommendationViewModel.snippetTitle = youtubeVideoModel.title
                videoRecommendationViewModel.snippetDescription = youtubeVideoModel.description
                videoRecommendationViewModel.thumbnailUrl = youtubeVideoModel.thumbnailUrl
                videoRecommendationViewModel.snippetChannel = youtubeVideoModel.channel
                videoRecommendationViewModel.duration = youtubeVideoModel.duration
                videoRecommendationViewModelList.add(videoRecommendationViewModel)
            }
        }
        return videoRecommendationViewModelList
    }
}