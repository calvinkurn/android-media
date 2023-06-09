package com.tokopedia.chatbot.chatbot2.domain.video

import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel

data class VideoUploadData(
    val videoPath: String?,
    val messageId: String,
    val startTime: String,
    val videoUploadUiModel: VideoUploadUiModel
)
