package com.tokopedia.home.beranda.presentation.view.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class HomeFragmentFactoryImpl : FragmentFactory() {
    private val SCROLL_RECOMMEND_LIST = "recommend_list"
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            HomeFragment::class.java.name -> HomeFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}