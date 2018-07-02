package com.tokopedia.product.edit.domain.mapper

import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel
import com.tokopedia.product.edit.view.viewmodel.VideoViewModel
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

    fun transformDataToVideoViewModel(youtubeVideoModel: YoutubeVideoModel): VideoViewModel {
        val videoViewModel = VideoViewModel()
        videoViewModel.videoID = youtubeVideoModel.id
        videoViewModel.snippetTitle = youtubeVideoModel.title
        videoViewModel.snippetDescription = youtubeVideoModel.description
        videoViewModel.thumbnailUrl = youtubeVideoModel.thumbnailUrl
        videoViewModel.snippetChannel = youtubeVideoModel.channel
        videoViewModel.duration = youtubeVideoModel.duration
        return videoViewModel
    }
}