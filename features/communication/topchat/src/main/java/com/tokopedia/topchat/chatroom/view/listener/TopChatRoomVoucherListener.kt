package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel

interface TopChatRoomVoucherListener {
    fun onClickVoucher(data: TopChatRoomVoucherUiModel, source: String)
    fun onImpressionVoucher(data: TopChatRoomVoucherUiModel, source: String)

    fun onClickBroadcastVoucher(
        broadcast: TopChatRoomBroadcastUiModel,
        voucher: TopChatRoomVoucherUiModel,
        position: Int = 0,
        total: Int = 1
    )
    fun onImpressionBroadcastVoucher(
        broadcast: TopChatRoomBroadcastUiModel,
        voucher: TopChatRoomVoucherUiModel,
        position: Int = 0,
        total: Int = 1
    )
}
