package com.tokopedia.chatbot.data.videoupload

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

class VideoUploadUiModel(
    builder: Builder
) : SendableUiModel(builder), Visitable<ChatbotTypeFactory> {

    var videoUrl: String? = builder.videoUrl
    var isRetry: Boolean = builder.isRetry
    var length: Long = builder.length

    init {
        this.videoUrl = builder.videoUrl
        this.isRetry = builder.isRetry
        this.length = builder.length
    }

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder : SendableUiModel.Builder<Builder, VideoUploadUiModel>() {
        internal var videoUrl: String? = null
        internal var isRetry: Boolean = false
        internal var length: Long = 0

        fun withVideoUrl(videoUrl: String): Builder {
            this.videoUrl = videoUrl
            return self()
        }

        fun withisRetry(isRetry: Boolean): Builder {
            this.isRetry = isRetry
            return self()
        }

        fun withLength(length: Long): Builder {
            this.length = length
            return self()
        }

        override fun build(): VideoUploadUiModel {
            return VideoUploadUiModel(this)
        }

    }
}
