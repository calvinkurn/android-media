package com.tokopedia.content.common.producttag.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.content.common.producttag.view.fragment.GlobalSearchProductTabFragment
import com.tokopedia.content.common.producttag.view.fragment.GlobalSearchShopTabFragment

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
class GlobalSearchResultPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader,
) : FragmentStatePagerAdapter(fragmentManager) {
    private val pageCount = 2

    override fun getCount(): Int {
        return pageCount
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> GlobalSearchProductTabFragment.getFragment(
                fragmentManager,
                classLoader,
            )
            else -> GlobalSearchShopTabFragment.getFragment(
                fragmentManager,
                classLoader,
            )
        }
    }
}