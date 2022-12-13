package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokochat_common.view.uimodel.base.TokoChatSendableBaseUiModel

/**
 * Primary constructor, use [Builder] class to create this instance.
 * Extend, do not edit
 */
open class TokoChatImageBubbleUiModel protected constructor(
    builder: Builder
): TokoChatSendableBaseUiModel(builder) {

    var imageId: String = builder.imageId
        private set
    var imagePath: String = builder.imagePath
        private set
    var shouldRetry: Boolean = builder.shouldRetry
        private set
    var isImageReady: Boolean = builder.isImageReady
        private set
    val impressHolder = ImpressHolder()

    fun updateImageData(imagePath: String, status: Boolean) {
        this.imagePath = imagePath
        this.isImageReady = status
    }

    fun updateShouldRetry(shouldRetry: Boolean) {
        this.shouldRetry = shouldRetry
    }

    open class Builder : TokoChatSendableBaseUiModel.Builder<Builder, TokoChatImageBubbleUiModel>() {

        internal var imageId = ""
        internal var imagePath = ""
        internal var shouldRetry = false
        internal var isImageReady = false

        fun withImageId(imageId: String): Builder {
            this.imageId = imageId
            return self()
        }

        fun withImageUrl(imagePath: String): Builder {
            this.imagePath = imagePath
            return self()
        }

        fun withShouldRetry(shouldRetry: Boolean): Builder {
            this.shouldRetry = shouldRetry
            return self()
        }

        override fun build(): TokoChatImageBubbleUiModel {
            return TokoChatImageBubbleUiModel(this)
        }
    }
}
