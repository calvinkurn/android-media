package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.tokochat_common.view.uimodel.base.TokoChatSendableBaseUiModel

class TokoChatMessageBubbleCensorUiModel(
    builder: Builder
) : TokoChatSendableBaseUiModel(builder) {

    open class Builder : TokoChatSendableBaseUiModel.Builder<Builder, TokoChatMessageBubbleCensorUiModel>() {
        override fun build(): TokoChatMessageBubbleCensorUiModel {
            return TokoChatMessageBubbleCensorUiModel(this)
        }
    }
}
