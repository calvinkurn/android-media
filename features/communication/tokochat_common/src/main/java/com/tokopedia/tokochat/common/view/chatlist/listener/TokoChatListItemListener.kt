package com.tokopedia.tokochat.common.view.chatlist.listener

import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel

interface TokoChatListItemListener {

    fun onClickChatItem(position: Int, element: TokoChatListItemUiModel)
}
