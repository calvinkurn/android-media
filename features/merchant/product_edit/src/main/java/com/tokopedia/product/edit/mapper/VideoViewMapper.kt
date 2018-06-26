package com.tokopedia.product.edit.mapper

import com.tokopedia.product.edit.viewmodel.VideoViewModel
import java.util.ArrayList

class VideoViewMapper {

    fun transform(videoIDs: List<String>): List<VideoViewModel> {
        val videoViewModelList = ArrayList<VideoViewModel>()
        for (videoID in videoIDs) {
            val videoViewModel = VideoViewModel()
            videoViewModel.videoID = videoID
            videoViewModelList.add(videoViewModel)
        }
        return videoViewModelList
    }
}