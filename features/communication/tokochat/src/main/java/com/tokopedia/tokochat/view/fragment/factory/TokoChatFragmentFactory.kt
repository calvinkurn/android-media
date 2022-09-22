package com.tokopedia.tokochat.view.fragment.factory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.tokochat.view.fragment.experiment.TokoChatFragmentExp
import com.tokopedia.tokochat.view.fragment.experiment.TokoChatListFragmentExp

class TokoChatFragmentFactory: FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            TokoChatFragmentExp::class -> TokoChatFragmentExp()
            TokoChatListFragmentExp::class -> TokoChatListFragmentExp()
            else -> super.instantiate(classLoader, className)
        }
    }
}
