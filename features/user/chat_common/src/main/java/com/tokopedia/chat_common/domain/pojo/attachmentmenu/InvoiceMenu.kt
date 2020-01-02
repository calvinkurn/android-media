package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.chat_common.R

class InvoiceMenu : AttachmentMenu(
        R.drawable.ic_invoice_purple_chat_common, "Invoice", "invoice"
) {

    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachInvoice(this)
    }

}