package com.tokopedia.inbox.universalinbox.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class UniversalInboxFragmentFactory: FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            UniversalInboxFragment::class.java -> UniversalInboxFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}
