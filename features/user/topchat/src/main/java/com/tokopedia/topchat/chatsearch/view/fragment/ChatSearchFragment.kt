package com.tokopedia.topchat.chatsearch.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.topchat.chatsearch.view.activity.ChatSearchActivity

/**
 * @author : Steven 2019-08-06
 */
class ChatSearchFragment : Fragment(), ChatSearchActivity.Listener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSearchQueryChanged(query: String) {

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