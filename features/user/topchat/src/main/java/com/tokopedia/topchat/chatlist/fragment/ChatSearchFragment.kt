package com.tokopedia.topchat.chatlist.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @author : Steven 2019-08-06
 */
class ChatSearchFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        const val SCREEN_NAME = "chat search"
        private const val CHAT_TAB_TITLE = "chat_tab_title"

        fun createFragment(): ChatSearchFragment {
            return ChatSearchFragment()
        }

    }
}