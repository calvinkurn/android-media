package com.tokopedia.topchat.idling

import androidx.fragment.app.FragmentManager
import androidx.test.espresso.IdlingResource
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub

class FragmentTransactionIdle(
        private val supportFragmentManager: FragmentManager, tag: String
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return "Wait fragment transaction to complete commit"
    }

    override fun isIdleNow(): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(TopChatRoomActivityStub.TAG)
        val isIdle = fragment != null && fragment.isResumed
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}