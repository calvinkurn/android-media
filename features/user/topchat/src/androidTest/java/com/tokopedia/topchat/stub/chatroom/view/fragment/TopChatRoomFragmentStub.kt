package com.tokopedia.topchat.stub.chatroom.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.stub.chatroom.view.customview.FakeTopChatViewStateImpl

open class TopChatRoomFragmentStub : TopChatRoomFragment() {

    /**
     * show bottomsheet immediately
     */
    override fun onRetrySendImage(element: ImageUploadViewModel) {
        super.onRetrySendImage(element)
        childFragmentManager.executePendingTransactions()
    }

    override fun onCreateViewState(view: View): BaseChatViewState {
        return FakeTopChatViewStateImpl(
                view, this, this, this,
                this, this, this, this,
                (activity as BaseChatToolbarActivity).getToolbar(), analytics,
        ).also {
            topchatViewState = it
        }
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