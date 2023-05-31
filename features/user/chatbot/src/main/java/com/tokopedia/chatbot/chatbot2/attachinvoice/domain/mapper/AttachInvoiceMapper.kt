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
            invoiceLinkAttributePojo.apply {
                code = selectedInvoice.code
                createTime = selectedInvoice.createdTime
                description = selectedInvoice.description
                hrefUrl = selectedInvoice.url
                id = selectedInvoice.id
                imageUrl = selectedInvoice.imageUrl
                status = selectedInvoice.status
                statusId = selectedInvoice.statusId
                title = selectedInvoice.title
                totalAmount = selectedInvoice.amount
                color = selectedInvoice.color
            }

            val invoiceLinkPojo = InvoiceLinkPojo()
            invoiceLinkPojo.type = selectedInvoice.typeString
            invoiceLinkPojo.typeId = selectedInvoice.type
            invoiceLinkPojo.attributes = invoiceLinkAttributePojo
            return invoiceLinkPojo
        }

        fun convertInvoiceToDomainInvoiceModel(selectedInvoice: SelectedInvoice): InvoiceLinkPojo {
            val invoiceLinkAttributePojo = InvoiceLinkAttributePojo()
            invoiceLinkAttributePojo.apply {
                code = selectedInvoice.invoiceNo.toString()
                createTime = selectedInvoice.date.toString()
                description = selectedInvoice.description.toString()
                hrefUrl = selectedInvoice.invoiceUrl.toString()
                id = selectedInvoice.invoiceId ?: 0
                imageUrl = selectedInvoice.topProductImage.toString()
                status = selectedInvoice.status.toString()
                statusId = selectedInvoice.statusId
                title = selectedInvoice.topProductName.toString()
                totalAmount = selectedInvoice.amount.toString()
                color = selectedInvoice.color.toString()
            }

            val invoiceLinkPojo = InvoiceLinkPojo()
            invoiceLinkPojo.type = selectedInvoice.invoiceTypeStr
            invoiceLinkPojo.typeId = selectedInvoice.invoiceType ?: 0
            invoiceLinkPojo.attributes = invoiceLinkAttributePojo
            return invoiceLinkPojo
        }
    }
}
