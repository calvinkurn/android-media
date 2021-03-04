package com.tokopedia.topchat.stub.chatroom.view.activity

import android.os.Bundle
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.stub.chatroom.di.ChatComponentStub
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub

class TopChatRoomActivityStub : TopChatRoomActivity() {

    lateinit var chatComponentStub: ChatComponentStub

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    override fun initializeChatComponent(): ChatComponent {
        return chatComponentStub
    }

    fun setupTestFragment(chatComponentStub: ChatComponentStub) {
        this.chatComponentStub = chatComponentStub
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, TAG)
                .commit()
    }

    override fun getTagFragment(): String {
        return TAG
    }

    override fun createChatRoomFragment(bundle: Bundle): BaseChatFragment {
        return TopChatRoomFragmentStub.createInstance(
                bundle
        )
    }

    companion object {
        const val TAG = "chatroom-tag"
    }
}