package com.tokopedia.chat_service.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.chat_service.view.activity.base.BaseTokoChatActivity
import com.tokopedia.chat_service.view.fragment.TokoChatListFragment

class TokoChatListActivity: BaseTokoChatActivity() {

    override fun getNewFragment(): Fragment {
        return TokoChatListFragment.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle()
        )
    }
}
