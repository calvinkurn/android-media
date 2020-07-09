package com.tokopedia.topchat.stub.chatlist.fragment

import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment
import com.tokopedia.topchat.stub.chatlist.di.DaggerChatListComponentStub

class ChatTabListFragmentStub: ChatTabListFragment() {
    override fun createChatListComponent(): ChatListComponent {
        return DaggerChatListComponentStub
                .builder()
                .build()
    }

    companion object {
        fun create(): ChatTabListFragment {
            return ChatTabListFragmentStub()
        }
    }
}