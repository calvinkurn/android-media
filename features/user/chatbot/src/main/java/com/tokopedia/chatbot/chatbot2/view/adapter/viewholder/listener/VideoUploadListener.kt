package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener

import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel

interface VideoUploadListener {
    fun onRetrySendVideo(element: VideoUploadUiModel)
    fun onVideoUploadCancelClicked(video: VideoUploadUiModel)
    fun onUploadedVideoClicked(videoUrl: String)
}
