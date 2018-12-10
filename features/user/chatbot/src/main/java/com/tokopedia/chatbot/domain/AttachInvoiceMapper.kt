package com.tokopedia.chatbot.domain

import com.tokopedia.chatbot.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.data.AttachInvoiceSingleViewModel

import javax.inject.Inject

/**
 * Created by Hendri on 28/03/18.
 */

class AttachInvoiceMapper @Inject
internal constructor() {
    companion object {

        @Deprecated("")
        fun selectedInvoiceViewModelToSelectedInvoice(viewModel: AttachInvoiceSingleViewModel): SelectedInvoice {
            return SelectedInvoice(viewModel.id,
                    viewModel.code,
                    viewModel.typeString,
                    viewModel.type,
                    viewModel.title,
                    viewModel.imageUrl,
                    viewModel.description,
                    viewModel.amount,
                    viewModel.createdTime,
                    viewModel.url,
                    viewModel.status,
                    viewModel.statusId)
        }

        fun invoiceViewModelToDomainInvoicePojo(selectedInvoice: AttachInvoiceSingleViewModel): InvoiceLinkPojo {
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

            val invoiceLinkPojo = InvoiceLinkPojo()
            invoiceLinkPojo.type = selectedInvoice.typeString
            invoiceLinkPojo.typeId = selectedInvoice.type
            invoiceLinkPojo.attributes = invoiceLinkAttributePojo
            return invoiceLinkPojo
        }

        fun convertInvoiceToDomainInvoiceModel(selectedInvoice: SelectedInvoice): InvoiceLinkPojo {
            val invoiceLinkAttributePojo = InvoiceLinkAttributePojo()
            invoiceLinkAttributePojo.code = selectedInvoice.invoiceNo
            invoiceLinkAttributePojo.createTime = selectedInvoice.date
            invoiceLinkAttributePojo.description = selectedInvoice.description
            invoiceLinkAttributePojo.hrefUrl = selectedInvoice.invoiceUrl
            invoiceLinkAttributePojo.id = selectedInvoice.invoiceId!!
            invoiceLinkAttributePojo.imageUrl = selectedInvoice.topProductImage
            invoiceLinkAttributePojo.status = selectedInvoice.status
            invoiceLinkAttributePojo.statusId = selectedInvoice.statusId
            invoiceLinkAttributePojo.title = selectedInvoice.topProductName
            invoiceLinkAttributePojo.totalAmount = selectedInvoice.amount

            val invoiceLinkPojo = InvoiceLinkPojo()
            invoiceLinkPojo.type = selectedInvoice.invoiceTypeStr
            invoiceLinkPojo.typeId = selectedInvoice.invoiceType!!
            invoiceLinkPojo.attributes = invoiceLinkAttributePojo
            return invoiceLinkPojo
        }
    }
}
