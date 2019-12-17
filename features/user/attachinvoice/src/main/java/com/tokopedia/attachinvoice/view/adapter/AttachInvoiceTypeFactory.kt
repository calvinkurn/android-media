package com.tokopedia.attachinvoice.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.attachinvoice.data.Invoice

interface AttachInvoiceTypeFactory : AdapterTypeFactory {
    fun type(invoice: Invoice): Int
}