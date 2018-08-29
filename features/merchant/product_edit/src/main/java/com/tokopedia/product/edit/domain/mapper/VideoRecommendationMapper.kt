package com.tokopedia.product.edit.domain.mapper

import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel

class VideoRecommendationMapper {
    fun transformDataToVideoViewModel(youtubeVideoModelList: List<YoutubeVideoModel>): List<VideoRecommendationViewModel> {
        return youtubeVideoModelList.filter { it.items != null && !it.items!!.isEmpty() }.map {
            VideoRecommendationViewModel().apply {
                videoID = it.id
                snippetTitle = it.title
                snippetDescription = it.description
                thumbnailUrl = it.thumbnailUrl
                snippetChannel = it.channel
                duration = it.duration
            }
        }
    }
}