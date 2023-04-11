package com.tokopedia.similarsearch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class SimilarSearchFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        if(className == SimilarSearchFragment::class.java.name) {
            return SimilarSearchFragment()
        }
        return super.instantiate(classLoader, className)
    }
}
