package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

interface TopChatVoucherListener {
    fun onVoucherClicked(data: TopChatVoucherUiModel, source: String)
}
