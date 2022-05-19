package com.tokopedia.inbox.fake.view.fragment

import com.tokopedia.inbox.view.activity.base.InboxTest
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.fragment.ChatListInboxFragment

class FakeChatListInboxFragment : ChatListInboxFragment() {
    override fun generateChatListComponent(): ChatListComponent {
        return InboxTest.chatListComponent!!
    }

    companion object {
        fun create(): FakeChatListInboxFragment {
            return FakeChatListInboxFragment()
        }
    }
}