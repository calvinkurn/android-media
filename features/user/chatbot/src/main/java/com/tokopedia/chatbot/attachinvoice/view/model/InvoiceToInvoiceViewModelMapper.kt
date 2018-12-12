package com.tokopedia.chatbot.attachinvoice.view.model


import com.tokopedia.chatbot.attachinvoice.domain.model.Invoice
import rx.functions.Func1
import java.util.*

/**
 * Created by Hendri on 22/03/18.
 */

class InvoiceToInvoiceViewModelMapper : Func1<List<Invoice>, List<InvoiceViewModel>> {
    override fun call(invoices: List<Invoice>): List<InvoiceViewModel> {
        val invoiceViewModels = ArrayList<InvoiceViewModel>()
        for (invoice in invoices) {
            val invoiceNumber = invoice.number
            val productTopName = invoice.title
            val productTopImage = invoice.imageUrl
            val status = invoice.status
            val date = invoice.date
            val total = invoice.total
            val productCountDisplay = invoice.desc
            val invoiceType = invoice.type
            val description = invoice.desc

            invoiceViewModels.add(InvoiceViewModel(
                    invoice.invoiceId,
                    invoice.invoiceTypeInt,
                    invoice.statusInt,
                    invoiceNumber,
                    productTopName,
                    productTopImage,
                    status,
                    date,
                    total,
                    invoiceType,
                    description,
                    invoice.url
            ))
        }
        return invoiceViewModels
    }
}
