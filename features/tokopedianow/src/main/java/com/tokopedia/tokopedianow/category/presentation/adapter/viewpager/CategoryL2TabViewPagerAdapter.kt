package com.tokopedia.tokopedianow.category.presentation.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryL2TabFragment

class CategoryL2TabViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var titleList: List<String> = emptyList()
    var components: List<Component> = emptyList()

    override fun getItemCount(): Int = titleList.count()

    override fun createFragment(position: Int): Fragment {
        return TokoNowCategoryL2TabFragment.newInstance(components)
    }
}
