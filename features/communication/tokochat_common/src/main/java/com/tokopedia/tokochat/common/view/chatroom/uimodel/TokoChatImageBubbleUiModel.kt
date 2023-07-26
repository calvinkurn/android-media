package com.tokopedia.tokochat.common.view.chatroom.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokochat.common.view.chatroom.uimodel.base.TokoChatSendableBaseUiModel

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
    var state: ImageState = ImageState.LOADING_LOAD // Image state, default is loading
        private set
    var isImageReady: Boolean = false // Flag for open image with image preview
        private set
    val impressHolder = ImpressHolder()

    fun updateImageData(imagePath: String, status: Boolean) {
        this.imagePath = imagePath
        this.isImageReady = status
    }

    fun updateImageState(state: ImageState) {
        this.state = state
    }

    enum class ImageState {
        LOADING_LOAD, LOADING_UPLOAD, ERROR_LOAD, ERROR_UPLOAD, SUCCESS
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
