package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.QuotationPojo
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class QuotationUiModel private constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory> {

    private val quotationPojo: QuotationPojo = builder.quotationPojo

    val quotationId get() = quotationPojo.identifier
    val price get() = quotationPojo.price
    val title get() = quotationPojo.title
    val thumbnailUrl get() = quotationPojo.thumbnail
    val url get() = quotationPojo.url

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder : SendableUiModel.Builder<Builder, QuotationUiModel>() {

        internal var quotationPojo = QuotationPojo()

        fun withQuotationPojo(quotationPojo: QuotationPojo): Builder {
            this.quotationPojo = quotationPojo
            return self()
        }

        override fun build(): QuotationUiModel {
            return QuotationUiModel(this)
        }
    }
}