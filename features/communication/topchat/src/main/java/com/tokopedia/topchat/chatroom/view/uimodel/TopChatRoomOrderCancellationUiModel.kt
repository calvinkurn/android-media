package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class TopChatRoomOrderCancellationUiModel private constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory> {

    val orderId = builder.orderId
    val title = builder.title
    val appLink = builder.appLink

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder : SendableUiModel.Builder<Builder, TopChatRoomOrderCancellationUiModel>() {

        internal var orderId: String = ""
        internal var title: String = ""
        internal var appLink: String = ""

        fun withOrderId(orderId: String): Builder {
            this.orderId = orderId
            return self()
        }

        fun withTitle(title: String): Builder {
            this.title = title
            return self()
        }

        fun withAppLink(appLink: String): Builder {
            this.appLink = appLink
            return self()
        }

        override fun build(): TopChatRoomOrderCancellationUiModel {
            return TopChatRoomOrderCancellationUiModel(this)
        }

    }
}
