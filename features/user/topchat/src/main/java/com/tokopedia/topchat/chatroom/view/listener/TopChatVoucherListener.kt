package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel

interface TopChatVoucherListener {
    fun onVoucherCopyClicked(voucherCode: String)
    fun onVoucherClicked(data: MerchantVoucherViewModel)
}
