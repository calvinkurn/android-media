package com.tokopedia.topchat.stub.chatlist.fragment

import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.view.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.view.fragment.ChatTabListFragment

class ChatTabListFragmentStub : ChatTabListFragment() {

    override fun createSellerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(
            ChatListQueriesConstant.PARAM_TAB_SELLER
        )
    }

    override fun createBuyerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(
            ChatListQueriesConstant.PARAM_TAB_USER
        )
    }

    override fun initToolTip() {
        // Don't initialize tool-tip
    }

    override fun isOnBoardingAlreadyShown(): Boolean {
        return true // Don't show onBoarding
    }
}