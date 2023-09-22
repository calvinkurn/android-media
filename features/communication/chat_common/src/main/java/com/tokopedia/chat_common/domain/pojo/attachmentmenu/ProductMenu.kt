package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.iconunify.IconUnify


class ProductMenu : AttachmentMenu(
    icon = IconUnify.PRODUCT,
    title = "Produk",
    label = "produk"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachProduct(this)
    }
}
