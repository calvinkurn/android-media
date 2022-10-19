package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.tokochat_common.view.uimodel.base.TokoChatSendableBaseUiModel

/**
 * Primary constructor, use [Builder] class to create this instance.
 * Extend, do not edit
 */
open class TokoChatImageBubbleUiModel protected constructor(
    builder: Builder
): TokoChatSendableBaseUiModel(builder) {

    var imageUrl: String = builder.imageUrl
        private set
    var shouldRetry: Boolean = builder.shouldRetry
        private set

    open class Builder : TokoChatSendableBaseUiModel.Builder<Builder, TokoChatImageBubbleUiModel>() {

        internal var imageUrl = ""
        internal var shouldRetry = false

        fun withImageUrl(imageUrl: String): Builder {
            this.imageUrl = imageUrl
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
