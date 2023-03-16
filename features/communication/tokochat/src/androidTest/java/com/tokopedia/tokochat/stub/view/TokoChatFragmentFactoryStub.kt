package com.tokopedia.tokochat.stub.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class TokoChatFragmentFactoryStub: FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            TokoChatFragmentStub::class.java -> TokoChatFragmentStub()
            else -> super.instantiate(classLoader, className)
        }
    }
}
