package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.chat_common.R

class ProductMenu : AttachmentMenu(
        R.drawable.ic_product_blue_chat_common, "Produk", "produk"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachProduct(this)
    }
}