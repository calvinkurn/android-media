package com.tokopedia.chatbot.attachinvoice.view.model


import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chatbot.attachinvoice.view.adapter.AttachInvoiceListAdapterTypeFactory

/**
 * Created by Hendri on 22/03/18.
 */

class InvoiceViewModel(var invoiceId: Long?, var invoiceType: Int, var statusId: Int,
                       var invoiceNumber: String, var productTopName: String,
                       var productTopImage: String, var status: String,
                       var date: String, var total: String, var invoiceTypeStr: String,
                       var description: String, var invoiceUrl: String)
    : Visitable<AttachInvoiceListAdapterTypeFactory> {

    override fun type(typeFactory: AttachInvoiceListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
