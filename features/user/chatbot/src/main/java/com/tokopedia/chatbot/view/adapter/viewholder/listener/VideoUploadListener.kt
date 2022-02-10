package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel

interface VideoUploadListener {
    fun onRetrySendVideo(element: VideoUploadUiModel)
    fun onVideoUploadCancelClicked(video : VideoUploadUiModel)
}