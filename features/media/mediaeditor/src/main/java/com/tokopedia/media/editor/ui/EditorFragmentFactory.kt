package com.tokopedia.media.editor.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject
import javax.inject.Provider

class EditorFragmentFactory @Inject constructor(
    private val fragmentProviders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        val fragment = fragmentProviders[fragmentClass]
        return fragment?.get() ?: super.instantiate(classLoader, className)
    }

}