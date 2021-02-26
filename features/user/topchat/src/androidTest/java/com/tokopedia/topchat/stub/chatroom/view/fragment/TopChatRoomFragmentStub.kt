package com.tokopedia.topchat.stub.chatroom.view.fragment

import android.os.Bundle
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment

class TopChatRoomFragmentStub : TopChatRoomFragment() {

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