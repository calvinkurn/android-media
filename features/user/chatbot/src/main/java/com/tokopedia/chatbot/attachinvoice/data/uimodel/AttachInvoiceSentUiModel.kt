package com.tokopedia.chatbot.attachinvoice.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceSentPojo
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory


class AttachInvoiceSentUiModel private constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<ChatbotTypeFactory>, DeferredAttachment {

    var imageUrl: String = builder.imageUrl
    var description: String = builder.description
    var totalAmount: String = builder.totalAmount
    var statusId: Int = builder.statusId
    var status: String = builder.status
    var invoiceId: String = builder.invoiceId
    var invoiceUrl: String = builder.invoiceUrl
    var createTime: String = builder.createTime
    var color: String = builder.color

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String get() = attachmentId

    init {
        if (!builder.needSync) {
            finishLoading()
        }
    }

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun updateData(attribute: Any?) {
        if (attribute is InvoiceSentPojo) {
            this.message = attribute.invoiceLink.attributes.title
            this.description = attribute.invoiceLink.attributes.description
            this.imageUrl = attribute.invoiceLink.attributes.imageUrl
            this.totalAmount = attribute.invoiceLink.attributes.totalAmount
            this.statusId = attribute.invoiceLink.attributes.statusId
            this.status = attribute.invoiceLink.attributes.status
            this.invoiceId = attribute.invoiceLink.attributes.code
            this.invoiceUrl = attribute.invoiceLink.attributes.hrefUrl
            this.createTime = attribute.invoiceLink.attributes.createTime
            this.isLoading = false
            this.color = attribute.invoiceLink.attributes.color
        }
    }

    override fun syncError() {
        this.isLoading = false
        this.isError = true
    }

    override fun finishLoading() {
        this.isLoading = false
        this.isError = false
    }

    class Builder : SendableUiModel.Builder<Builder, AttachInvoiceSentUiModel>() {

        internal var imageUrl: String = ""
        internal var description: String = ""
        internal var totalAmount: String = ""
        internal var statusId: Int = -1
        internal var status: String = ""
        internal var invoiceId: String = ""
        internal var invoiceUrl: String = ""
        internal var createTime: String = ""
        internal var needSync: Boolean = true
        internal var color: String = ""

        fun withInvoiceAttributesResponse(invoice: InvoiceLinkPojo): Builder {
            withMsg(invoice.attributes.title)
            withDescription(invoice.attributes.description)
            withImageUrl(invoice.attributes.imageUrl)
            withTotalAmount(invoice.attributes.totalAmount)
            withStatusId(invoice.attributes.statusId)
            withStatus(invoice.attributes.status)
            withInvoiceId(invoice.attributes.code)
            withInvoiceUrl(invoice.attributes.hrefUrl)
            withCreateTime(invoice.attributes.createTime)
            withColor(invoice.attributes.color)
            return self()
        }

        fun withImageUrl(imageUrl: String): Builder {
            this.imageUrl = imageUrl
            return self()
        }

        fun withDescription(description: String): Builder {
            this.description = description
            return self()
        }

        fun withTotalAmount(totalAmount: String): Builder {
            this.totalAmount = totalAmount
            return self()
        }

        fun withStatusId(statusId: Int): Builder {
            this.statusId = statusId
            return self()
        }

        fun withStatus(status: String): Builder {
            this.status = status
            return self()
        }

        fun withInvoiceId(invoiceId: String): Builder {
            this.invoiceId = invoiceId
            return self()
        }

        fun withInvoiceUrl(invoiceUrl: String): Builder {
            this.invoiceUrl = invoiceUrl
            return self()
        }

        fun withCreateTime(createTime: String): Builder {
            this.createTime = createTime
            return self()
        }

        fun withNeedSync(needSync: Boolean): Builder {
            this.needSync = needSync
            return self()
        }

        fun withColor(color : String) : Builder {
            this.color = color
            return self()
        }

        override fun build(): AttachInvoiceSentUiModel {
            return AttachInvoiceSentUiModel(this)
        }
    }
}
