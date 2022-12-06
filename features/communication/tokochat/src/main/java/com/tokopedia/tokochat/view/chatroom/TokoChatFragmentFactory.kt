package com.tokopedia.tokochat.view.chatroom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class TokoChatFragmentFactory: FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            TokoChatFragment::class.java -> TokoChatFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}
