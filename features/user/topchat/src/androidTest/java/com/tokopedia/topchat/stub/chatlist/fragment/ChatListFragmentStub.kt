package com.tokopedia.topchat.stub.chatlist.fragment

import android.os.Bundle
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment

class ChatListFragmentStub: ChatListFragment() {

    companion object {
        fun createFragment(title: String): ChatListFragment {
            val bundle = Bundle().apply {
                putString(CHAT_TAB_TITLE, title)
            }
            return ChatListFragment().apply {
                arguments = bundle
            }
        }
    }
}