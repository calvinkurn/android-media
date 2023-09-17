package com.tokopedia.tokopedianow.category.presentation.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryL2TabFragment

class CategoryL2TabViewPagerAdapter(
    fragment: Fragment,
    private val fragments: List<TokoNowCategoryL2TabFragment>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragments.count()

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
