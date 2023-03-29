package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokochat_common.view.uimodel.base.TokoChatSendableBaseUiModel

/**
 * Primary constructor, use [Builder] class to create this instance.
 * Extend, do not edit
 */
open class TokoChatImageBubbleUiModel protected constructor(
    builder: Builder
) : TokoChatSendableBaseUiModel(builder) {

    var imageId: String = builder.imageId
        private set
    var imagePath: String = ""
        private set
    var shouldRetryLoad: Boolean = false
        private set
    var isImageReady: Boolean = false
        private set
    val impressHolder = ImpressHolder()

    fun updateImageData(imagePath: String, status: Boolean) {
        this.imagePath = imagePath
        this.isImageReady = status
    }

    fun updateShouldRetry(shouldRetry: Boolean) {
        this.shouldRetryLoad = shouldRetry
    }

    open class Builder : TokoChatSendableBaseUiModel.Builder<Builder, TokoChatImageBubbleUiModel>() {

        internal var imageId = ""

        fun withImageId(imageId: String): Builder {
            this.imageId = imageId
            return self()
        }

        override fun build(): TokoChatImageBubbleUiModel {
            return TokoChatImageBubbleUiModel(this)
        }
    }
}
