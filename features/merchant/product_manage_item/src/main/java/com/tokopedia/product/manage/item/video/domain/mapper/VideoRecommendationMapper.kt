package com.tokopedia.product.manage.item.video.domain.mapper

import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel

class VideoRecommendationMapper {
    fun transformDataToVideoViewModel(youtubeVideoModelList: List<YoutubeVideoDetailModel>): List<VideoRecommendationViewModel> {
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