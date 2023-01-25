package com.tokopedia.tokochat.stub.view

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.view.chatroom.TokoChatFragment

class TokoChatFragmentStub: TokoChatFragment() {

    override fun initializeChatRoom(savedInstanceState: Bundle?) {
        super.initializeChatRoom(savedInstanceState)
        BaseTokoChatTest.idlingResourceGroupBooking.increment()
    }

    override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
        super.onGroupBookingChannelCreationError(error)
        BaseTokoChatTest.idlingResourceGroupBooking.decrement()
    }

    override fun markChatAsRead() {
        super.markChatAsRead()
        BaseTokoChatTest.idlingResourceGroupBooking.decrement()
    }

    companion object {
        private const val TAG = "TokoChatFragmentStub"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): TokoChatFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? TokoChatFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                TokoChatFragment::class.java.name
            ).apply {
                arguments = bundle
            } as TokoChatFragment
        }
    }
}
