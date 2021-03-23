package com.tokopedia.inbox.view.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.notifcenter.presentation.fragment.NotificationFragment
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxFragment
import com.tokopedia.topchat.chatlist.fragment.ChatListInboxFragment

interface InboxFragmentFactory {
    fun createChatListFragment(): Fragment
    fun createNotificationFragment(): Fragment
    fun createTalkInboxFragment(): Fragment
}

class InboxFragmentFactoryImpl : InboxFragmentFactory {
    override fun createChatListFragment(): Fragment {
        return ChatListInboxFragment.createFragment()
    }

    override fun createNotificationFragment(): Fragment {
        return NotificationFragment()
    }

    override fun createTalkInboxFragment(): Fragment {
        return  TalkInboxFragment.createNewInstance()
    }
}