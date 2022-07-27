package com.tokopedia.topchat.stub.chatlist.activity

import androidx.fragment.app.Fragment
import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity
import com.tokopedia.topchat.stub.chatlist.fragment.ChatTabListFragmentStub

class ChatListActivityStub : ChatListActivity() {

    override fun initializeChatListComponent(): ChatListComponent {
        return ChatListTest.chatListComponentStub!!
    }

    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun getNewFragment(): Fragment {
        return ChatTabListFragmentStub()
    }
}