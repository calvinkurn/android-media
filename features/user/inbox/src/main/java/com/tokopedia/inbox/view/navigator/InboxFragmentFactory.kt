package com.tokopedia.inbox.view.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.notifcenter.presentation.fragment.NotificationFragment
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment

interface InboxFragmentFactory {
    fun createChatListFragment(): Fragment
    fun createNotificationFragment(): Fragment
}

class InboxFragmentFactoryImpl : InboxFragmentFactory{
    override fun createChatListFragment(): Fragment {
        return ChatTabListFragment.create()
    }

    override fun createNotificationFragment(): Fragment {
        return NotificationFragment()
    }
}