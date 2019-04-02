package com.tokopedia.chat_common.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ChatroomViewModel

/**
 * @author by nisie on 10/12/18.
 */
interface BaseChatViewState {

    fun initView()

    fun onShowStartTyping()

    fun onShowStopTyping()

    fun onReceiveMessageEvent(visitable: Visitable<*>)

    fun updateHeader(chatroomViewModel: ChatroomViewModel, onToolbarClicked: () -> Unit)

    fun onSendingMessage(messageId: String, userId: String, name: String, sendMessage: String,
                         startTime: String)

    fun removeDummyIfExist(successVisitable: Visitable<*>)

    fun removeMessageOnReplyBox()

    fun onReceiveRead()

}