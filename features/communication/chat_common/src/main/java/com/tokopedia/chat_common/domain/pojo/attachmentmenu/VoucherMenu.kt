package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.iconunify.IconUnify

class VoucherMenu : AttachmentMenu(
    icon = IconUnify.PRODUCT,
    title = "Voucher",
    label = "voucher"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachVoucher(this)
    }
}
