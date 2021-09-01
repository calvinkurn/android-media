package com.tokopedia.topchat.stub.chatroom.view.activity

import android.os.Bundle
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub

class TopChatRoomActivityStub : TopChatRoomActivity() {

    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun initializeChatComponent(): ChatComponent {
        return TopchatRoomTest.chatComponentStub!!
    }

    override fun createChatRoomFragment(bundle: Bundle): BaseChatFragment {
        return TopChatRoomFragmentStub.createInstance(bundle)
    }
}