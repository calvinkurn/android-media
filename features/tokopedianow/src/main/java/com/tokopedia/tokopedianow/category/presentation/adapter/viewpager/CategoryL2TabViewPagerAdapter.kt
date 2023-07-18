package com.tokopedia.tokopedianow.category.presentation.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryL2TabFragment

class CategoryL2TabViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var titleList: List<String> = emptyList()
    var components: List<Component> = emptyList()
    var categoryL2Ids: List<String> = emptyList()

    override fun getItemCount(): Int = titleList.count()

    override fun createFragment(position: Int): Fragment {
        val categoryIdL2 = categoryL2Ids.getOrNull(position).orEmpty()
        return TokoNowCategoryL2TabFragment.newInstance(categoryIdL2, components)
    }
}
