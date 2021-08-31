package com.tokopedia.topchat.stub.chatlist.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub

class TopChatRoomFragmentForChatListStub: TopChatRoomFragmentStub() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ChatListTest.chatRoomIdling.increment()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSuccessGetExistingChatFirstTime(chatRoom: ChatroomViewModel, chat: ChatReplies) {
        super.onSuccessGetExistingChatFirstTime(chatRoom, chat)
        ChatListTest.chatRoomIdling.decrement()
    }

    companion object {
        fun createInstance(
            bundle: Bundle
        ): TopChatRoomFragmentForChatListStub {
            return TopChatRoomFragmentForChatListStub().apply {
                arguments = bundle
            }
        }
    }
}