package com.tokopedia.tokochat.common.view.chatroom.uimodel

import com.tokopedia.tokochat.common.view.chatroom.uimodel.base.TokoChatSendableBaseUiModel

class TokoChatMessageBubbleCensorUiModel(
    builder: Builder
) : TokoChatSendableBaseUiModel(builder) {

    open class Builder : TokoChatSendableBaseUiModel.Builder<Builder, TokoChatMessageBubbleCensorUiModel>() {
        override fun build(): TokoChatMessageBubbleCensorUiModel {
            return TokoChatMessageBubbleCensorUiModel(this)
        }
    }
}
