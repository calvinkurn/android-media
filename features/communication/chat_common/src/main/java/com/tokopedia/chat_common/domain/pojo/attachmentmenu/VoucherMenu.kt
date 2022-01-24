package com.tokopedia.chat_common.domain.pojo.attachmentmenu

import com.tokopedia.chat_common.R

class VoucherMenu : AttachmentMenu(
        R.drawable.ic_chat_common_attach_voucher, "Voucher", "voucher"
) {
    override fun onClick(listener: AttachmentMenuListener) {
        listener.onClickAttachVoucher(this)
    }
}