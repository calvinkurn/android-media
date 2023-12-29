package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.iconunify.IconUnify

class InvoiceMenu : AttachmentMenu(
    icon = IconUnify.LIST_TRANSACTION,
    title = "Invoice",
    label = "invoice"
) {

    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachInvoice(this)
    }

}
