package com.tokopedia.topchat.stub.chatlist.fragment

import android.os.Bundle
import com.tokopedia.topchat.chatlist.view.fragment.ChatListFragment

class ChatListFragmentStub : ChatListFragment() {

    /**
     * Workaround for [com.tokopedia.unifycomponents.LoaderUnify] doesn't support
     * for no animation setting. This prevent the adapter showing loading state.
     * Need to Ask unify team to add support for no animation device
     */
    override fun showLoading() {
        adapter?.removeErrorNetwork()
        hideSnackBarRetry()
    }

    companion object {
        fun createFragment(title: String): ChatListFragment {
            val bundle = Bundle().apply {
                putString(CHAT_TAB_TITLE, title)
            }
            return ChatListFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}