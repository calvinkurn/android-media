package com.tokopedia.product.edit.mapper

import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel
import com.tokopedia.product.edit.viewmodel.VideoViewModel
import java.util.ArrayList

class VideoMapper {

    fun transformDataToVideoViewModel(youtubeVideoModelList: List<YoutubeVideoModel>): List<VideoViewModel> {
        val videoViewModelList = ArrayList<VideoViewModel>()
        for (youtubeVideoModel in youtubeVideoModelList) {
            val videoViewModel = VideoViewModel()
            videoViewModel.videoID = youtubeVideoModel.id
            videoViewModel.snippetTitle = youtubeVideoModel.title
            videoViewModel.snippetDescription = youtubeVideoModel.description
            videoViewModel.thumbnailUrl = youtubeVideoModel.thumbnailUrl
            videoViewModel.snippetChannel = youtubeVideoModel.channel
            videoViewModel.duration = youtubeVideoModel.duration
            videoViewModelList.add(videoViewModel)
        }
        return videoViewModelList
    }

    fun transformVideoRecommendationViewModelToVideoViewModel(videoRecommendationViewModel : VideoRecommendationViewModel): VideoViewModel {
        val videoViewModel = VideoViewModel()
        videoViewModel.videoID = videoRecommendationViewModel.videoID
        videoViewModel.snippetTitle = videoRecommendationViewModel.snippetTitle
        videoViewModel.snippetDescription = videoRecommendationViewModel.snippetDescription
        videoViewModel.thumbnailUrl = videoRecommendationViewModel.thumbnailUrl
        videoViewModel.snippetChannel = videoRecommendationViewModel.snippetChannel
        videoViewModel.duration = videoRecommendationViewModel.duration
        videoViewModel.recommendation = true
        return videoViewModel
    }
}