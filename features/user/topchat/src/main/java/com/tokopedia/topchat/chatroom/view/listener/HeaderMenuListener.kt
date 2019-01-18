package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.chat_common.data.BlockedStatus

/**
 * @author by nisie on 10/01/19.
 */
interface HeaderMenuListener{

    fun onGoToShop()

    fun onDeleteConversation()

    fun onGoToChatSetting(blockedStatus: BlockedStatus)

}