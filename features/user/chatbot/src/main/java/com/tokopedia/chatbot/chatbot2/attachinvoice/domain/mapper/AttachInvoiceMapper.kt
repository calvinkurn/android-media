package com.tokopedia.chatbot.chatbot2.attachinvoice.domain.mapper

import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkAttributePojo
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.chatbot2.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSingleUiModel
import javax.inject.Inject

/**
 * Created by Hendri on 28/03/18.
 */

class AttachInvoiceMapper @Inject
internal constructor() {
    companion object {

        fun invoiceViewModelToDomainInvoicePojo(selectedInvoice: AttachInvoiceSingleUiModel): InvoiceLinkPojo {
            val invoiceLinkAttributePojo = InvoiceLinkAttributePojo()
            invoiceLinkAttributePojo.code = selectedInvoice.code
            invoiceLinkAttributePojo.createTime = selectedInvoice.createdTime
            invoiceLinkAttributePojo.description = selectedInvoice.description
            invoiceLinkAttributePojo.hrefUrl = selectedInvoice.url
            invoiceLinkAttributePojo.id = selectedInvoice.id
            invoiceLinkAttributePojo.imageUrl = selectedInvoice.imageUrl
            invoiceLinkAttributePojo.status = selectedInvoice.status
            invoiceLinkAttributePojo.statusId = selectedInvoice.statusId
            invoiceLinkAttributePojo.title = selectedInvoice.title
            invoiceLinkAttributePojo.totalAmount = selectedInvoice.amount
            invoiceLinkAttributePojo.color = selectedInvoice.color

            val invoiceLinkPojo = InvoiceLinkPojo()
            invoiceLinkPojo.type = selectedInvoice.typeString
            invoiceLinkPojo.typeId = selectedInvoice.type
            invoiceLinkPojo.attributes = invoiceLinkAttributePojo
            return invoiceLinkPojo
        }

        fun convertInvoiceToDomainInvoiceModel(selectedInvoice: SelectedInvoice): InvoiceLinkPojo {
            val invoiceLinkAttributePojo = InvoiceLinkAttributePojo()
            invoiceLinkAttributePojo.code = selectedInvoice.invoiceNo.toString()
            invoiceLinkAttributePojo.createTime = selectedInvoice.date.toString()
            invoiceLinkAttributePojo.description = selectedInvoice.description.toString()
            invoiceLinkAttributePojo.hrefUrl = selectedInvoice.invoiceUrl.toString()
            invoiceLinkAttributePojo.id = selectedInvoice.invoiceId ?: 0
            invoiceLinkAttributePojo.imageUrl = selectedInvoice.topProductImage.toString()
            invoiceLinkAttributePojo.status = selectedInvoice.status.toString()
            invoiceLinkAttributePojo.statusId = selectedInvoice.statusId
            invoiceLinkAttributePojo.title = selectedInvoice.topProductName.toString()
            invoiceLinkAttributePojo.totalAmount = selectedInvoice.amount.toString()
            invoiceLinkAttributePojo.color = selectedInvoice.color.toString()

            val invoiceLinkPojo = InvoiceLinkPojo()
            invoiceLinkPojo.type = selectedInvoice.invoiceTypeStr
            invoiceLinkPojo.typeId = selectedInvoice.invoiceType ?: 0
            invoiceLinkPojo.attributes = invoiceLinkAttributePojo
            return invoiceLinkPojo
        }
    }
}
