package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomTypeFactory

class TopChatRoomOrderCancellationUiModel private constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatRoomTypeFactory> {

    val orderId = builder.orderId
    val orderStatus = builder.orderStatus
    val invoiceId = builder.invoiceId
    val title = builder.title
    val appLink = builder.appLink

    override fun type(typeFactory: TopChatRoomTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder : SendableUiModel.Builder<Builder, TopChatRoomOrderCancellationUiModel>() {

        internal var orderId: String = ""
        internal var orderStatus: String = ""
        internal var invoiceId: String = ""
        internal var title: String = ""
        internal var appLink: String = ""

        fun withOrderId(orderId: String): Builder {
            this.orderId = orderId
            return self()
        }

        fun withOrderStatus(orderStatus: String): Builder {
            this.orderStatus = orderStatus
            return self()
        }

        fun withInvoiceId(invoiceId: String): Builder {
            this.invoiceId = invoiceId
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
