package com.tokopedia.product.manage.item.video.domain.mapper

import com.tokopedia.product.manage.item.video.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel

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