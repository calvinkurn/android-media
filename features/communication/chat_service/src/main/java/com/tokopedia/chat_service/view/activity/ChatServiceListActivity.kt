package com.tokopedia.chat_service.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.chat_service.view.activity.base.BaseChatServiceActivity
import com.tokopedia.chat_service.view.fragment.ChatServiceListFragment

class ChatServiceListActivity: BaseChatServiceActivity() {

    override fun getNewFragment(): Fragment {
        return ChatServiceListFragment.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle()
        )
    }
}
