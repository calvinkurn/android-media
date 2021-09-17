package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceLinkPojo
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceSentPojo
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * Created by Hendri on 27/03/18.
 */

class AttachInvoiceSentViewModel private constructor(
    builder: Builder
) : SendableViewModel(builder), Visitable<BaseChatTypeFactory>, DeferredAttachment {

    var imageUrl: String? = null
    var description: String? = null
    var totalAmount: String? = null
    var statusId: Int? = null
    var status: String? = null
    var invoiceId: String? = null
    var invoiceUrl: String? = null
    var createTime: String? = null

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String get() = attachmentId

    init {
        this.description = builder.description
        this.imageUrl = builder.imageUrl
        this.totalAmount = builder.totalAmount
        this.statusId = builder.statusId
        this.status = builder.status
        this.invoiceId = builder.invoiceId
        this.invoiceUrl = builder.invoiceUrl
        this.createTime = builder.createTime
        if (!builder.needSync) {
            finishLoading()
        }
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
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

    class Builder : SendableViewModel.Builder<Builder, AttachInvoiceSentViewModel>() {

        internal var imageUrl: String = ""
        internal var description: String = ""
        internal var totalAmount: String = ""
        internal var statusId: Int = -1
        internal var status: String = ""
        internal var invoiceId: String = ""
        internal var invoiceUrl: String = ""
        internal var createTime: String = ""
        internal var needSync: Boolean = true

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

        override fun build(): AttachInvoiceSentViewModel {
            return AttachInvoiceSentViewModel(this)
        }
    }
}
