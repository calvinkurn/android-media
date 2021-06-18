package com.tokopedia.topchat.stub.chatroom.view.fragment

import android.os.Bundle
import android.view.View
import androidx.test.espresso.idling.CountingIdlingResource
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.stub.chatroom.view.customview.FakeTopChatViewStateImpl

class TopChatRoomFragmentStub : TopChatRoomFragment() {

    lateinit var keyboardStateIdling: CountingIdlingResource

    override fun onCreateViewState(view: View): BaseChatViewState {
        return FakeTopChatViewStateImpl(
                view, this, this, this,
                this, this, this, this,
                (activity as BaseChatToolbarActivity).getToolbar(), analytics,
                keyboardStateIdling
        ).also {
            topchatViewState = it
        }
    }

    companion object {
        fun createInstance(
                bundle: Bundle,
                keyboardStateIdling: CountingIdlingResource
        ): TopChatRoomFragmentStub {
            return TopChatRoomFragmentStub().apply {
                arguments = bundle
                this.keyboardStateIdling = keyboardStateIdling
            }
        }
    }
}