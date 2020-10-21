package com.tokopedia.inbox.view.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment

interface InboxFragmentFactory {
    fun createChatListFragment(): Fragment
}

class InboxFragmentFactoryImpl : InboxFragmentFactory{
    override fun createChatListFragment(): Fragment {
        return ChatTabListFragment.create()
    }
}