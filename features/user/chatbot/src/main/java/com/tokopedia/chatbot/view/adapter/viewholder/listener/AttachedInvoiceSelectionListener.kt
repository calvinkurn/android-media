package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceLinkPojo

/**
 * @author by nisie on 06/12/18.
 */
interface AttachedInvoiceSelectionListener {
    fun onInvoiceSelected(invoiceLinkPojo: InvoiceLinkPojo)
}