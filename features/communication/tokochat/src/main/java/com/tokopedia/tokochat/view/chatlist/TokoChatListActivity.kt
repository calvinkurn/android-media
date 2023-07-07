package com.tokopedia.tokochat.view.chatlist

import androidx.fragment.app.Fragment
import com.tokopedia.tokochat.di.TokoChatActivityComponentFactory
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat_common.view.chatlist.TokoChatListBaseActivity

class TokoChatListActivity: TokoChatListBaseActivity<TokoChatComponent>() {

    override fun getNewFragment(): Fragment? {
        return Fragment()
    }

    override fun getComponent(): TokoChatComponent {
        return tokoChatComponent ?: initializeTokoChatComponent()
    }

    private fun initializeTokoChatComponent(): TokoChatComponent {
        return TokoChatActivityComponentFactory
            .instance
            .createTokoChatComponent(application).also {
                tokoChatComponent = it
            }
    }
}
