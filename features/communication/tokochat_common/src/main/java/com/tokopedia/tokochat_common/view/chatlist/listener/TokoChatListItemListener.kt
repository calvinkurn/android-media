package com.tokopedia.tokochat_common.view.chatlist.listener

import com.tokopedia.tokochat_common.view.chatlist.uimodel.TokoChatListItemUiModel

interface TokoChatListItemListener {

    fun onClickChatItem(position: Int, element: TokoChatListItemUiModel)
}
