package com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.chatbot2.data.owocinvoice.DynamicOwocInvoicePojo
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotTypeFactory

class DynamicOwocInvoiceUiModel(
    builder: Builder
) : SendableUiModel(builder), Visitable<ChatbotTypeFactory> {

    var invoiceList: List<DynamicOwocInvoicePojo.InvoiceCardOwoc>? = builder.invoiceList

    class Builder : SendableUiModel.Builder<Builder, DynamicOwocInvoiceUiModel>() {

        internal var invoiceList: List<DynamicOwocInvoicePojo.InvoiceCardOwoc>? = null

        fun withOwocInvoiceList(list: List<DynamicOwocInvoicePojo.InvoiceCardOwoc>?): Builder {
            this.invoiceList = list
            return self()
        }

        override fun build(): DynamicOwocInvoiceUiModel {
            return DynamicOwocInvoiceUiModel(
                this
            )
        }
    }

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
