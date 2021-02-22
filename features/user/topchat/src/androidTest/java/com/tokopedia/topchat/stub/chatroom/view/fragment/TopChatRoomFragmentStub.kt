package com.tokopedia.topchat.stub.chatroom.view.fragment

import android.os.Bundle
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub

class TopChatRoomFragmentStub : TopChatRoomFragment() {

    override fun initInjector() {
        (activity as TopChatRoomActivityStub)
                .chatComponentStub
                .inject(this)
    }

    companion object {
        fun createInstance(
                bundle: Bundle
        ): TopChatRoomFragmentStub {
            return TopChatRoomFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}