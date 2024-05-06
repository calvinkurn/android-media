package com.tokopedia.product.preview.factory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class ProductPreviewFragmentFactoryUITest(
    private val fragmentProviders: Map<Class<out Fragment>, () -> Fragment>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        val fragment = fragmentProviders[fragmentClass]
        return fragment?.invoke() ?: super.instantiate(classLoader, className)
    }
}
