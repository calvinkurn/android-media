package com.tokopedia.topchat.stub.chatroom.view.activity

import android.os.Bundle
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub

class TopChatRoomActivityStub : TopChatRoomActivity() {

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    fun setupTestFragment() {
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, tagFragment)
                .commit()
    }

    override fun createChatRoomFragment(bundle: Bundle): BaseChatFragment {
        return TopChatRoomFragmentStub.createInstance(bundle)
    }
}