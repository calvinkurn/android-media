package com.tokopedia.tokochat.stub.view

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.view.chatroom.TokoChatFragment

class TokoChatFragmentStub : TokoChatFragment() {

    override fun initializeChatRoom(savedInstanceState: Bundle?) {
        super.initializeChatRoom(savedInstanceState)
        BaseTokoChatTest.idlingResourceGroupBooking.increment()
    }

    override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
        super.onGroupBookingChannelCreationError(error)
        if (!BaseTokoChatTest.idlingResourceGroupBooking.isIdleNow) {
            BaseTokoChatTest.idlingResourceGroupBooking.decrement()
        }
    }

    override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
        super.onGroupBookingChannelCreationSuccess(channelUrl)
        if (!BaseTokoChatTest.idlingResourceGroupBooking.isIdleNow) {
            BaseTokoChatTest.idlingResourceGroupBooking.decrement()
        }
    }

    companion object {
        private const val TAG = "TokoChatFragmentStub"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): TokoChatFragmentStub {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? TokoChatFragmentStub
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                TokoChatFragmentStub::class.java.name
            ).apply {
                arguments = bundle
            } as TokoChatFragmentStub
        }
    }
}
