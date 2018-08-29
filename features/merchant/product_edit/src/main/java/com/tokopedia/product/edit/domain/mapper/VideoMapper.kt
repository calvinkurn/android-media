package com.tokopedia.product.edit.domain.mapper

import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel
import com.tokopedia.product.edit.view.viewmodel.VideoViewModel
import java.util.ArrayList

class VideoMapper {

    fun transformDataToVideoViewModel(youtubeVideoModelList: List<YoutubeVideoModel>): List<VideoViewModel> {
        return youtubeVideoModelList.map {
            VideoViewModel().apply {
                videoID = it.id
                snippetTitle = it.title
                snippetDescription = it.description
                thumbnailUrl = it.thumbnailUrl
                snippetChannel = it.channel
                duration = it.duration
            }
        }
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