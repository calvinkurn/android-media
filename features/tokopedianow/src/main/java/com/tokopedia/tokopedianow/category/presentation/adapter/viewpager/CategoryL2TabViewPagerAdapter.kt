package com.tokopedia.tokopedianow.category.presentation.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryL2TabFragment

class CategoryL2TabViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = mutableListOf<TokoNowCategoryL2TabFragment>()

    private var selectedTabPosition: Int = 0

    override fun getItemCount(): Int = fragments.count()

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(fragment: TokoNowCategoryL2TabFragment) {
        fragments.add(fragment)
    }

    fun setSelectedTabPosition(position: Int) {
        selectedTabPosition = position
    }

    fun clearFragments() {
        fragments.clear()
    }

    fun getSelectedTabFragment(): TokoNowCategoryL2TabFragment {
        return fragments[selectedTabPosition]
    }
}
