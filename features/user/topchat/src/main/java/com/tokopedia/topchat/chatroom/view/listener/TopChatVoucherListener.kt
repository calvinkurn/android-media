package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel

interface TopChatVoucherListener {
    fun onVoucherCopyClicked(voucherCode: String, messageId: String, replyId: String, blastId: String, attachmentId: String, replyTime: String?, fromUid: String?)
    fun onVoucherClicked(data: MerchantVoucherViewModel)
}
