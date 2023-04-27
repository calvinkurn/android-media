package com.tokopedia.tokochat.stub.view

import androidx.fragment.app.Fragment
import com.tokopedia.tokochat.view.chatroom.TokoChatActivity

class TokoChatActivityStub : TokoChatActivity() {

    override fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = TokoChatFragmentFactoryStub()
    }

    override fun getNewFragment(): Fragment {
        return TokoChatFragmentStub.getFragment(
            supportFragmentManager,
            classLoader,
            getFragmentBundle()
        )
    }
}
