package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel

interface TopChatRoomVoucherListener {
    fun onClickVoucher(data: TopChatRoomVoucherUiModel, source: String)
    fun onImpressionVoucher(data: TopChatRoomVoucherUiModel, source: String)
}
