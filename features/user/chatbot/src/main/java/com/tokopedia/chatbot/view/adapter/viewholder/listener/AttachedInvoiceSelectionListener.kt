package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo

/**
 * @author by nisie on 06/12/18.
 */
interface AttachedInvoiceSelectionListener {
    fun onInvoiceSelected(invoiceLinkPojo: InvoiceLinkPojo)
}