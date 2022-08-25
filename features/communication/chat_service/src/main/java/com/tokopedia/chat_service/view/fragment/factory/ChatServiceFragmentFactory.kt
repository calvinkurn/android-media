package com.tokopedia.chat_service.view.fragment.factory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.chat_service.view.fragment.ChatServiceFragment

class ChatServiceFragmentFactory: FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            ChatServiceFragment::class -> ChatServiceFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}