package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener

import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkPojo

/**
 * @author by nisie on 06/12/18.
 */
interface AttachedInvoiceSelectionListener {
    fun onInvoiceSelected(invoiceLinkPojo: InvoiceLinkPojo)
}
