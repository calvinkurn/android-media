package com.tokopedia.chat_service.view.fragment.factory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.chat_service.view.fragment.TokoChatFragment

class TokoChatFragmentFactory: FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            TokoChatFragment::class -> TokoChatFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}
