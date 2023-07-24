package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.adapter.viewpager.CategoryL2TabViewPagerAdapter
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryL2PageChangeCallback
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryL2TabFragment
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryL2TabBinding
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.view.binding.viewBinding

class CategoryL2TabViewHolder(
    itemView: View,
    private val listener: CategoryL2TabListener,
    private val tokoNowView: TokoNowView
) : AbstractViewHolder<CategoryL2TabUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.item_tokopedianow_category_l2_tab
    }

    private val viewPagerAdapter by lazy {
        CategoryL2TabViewPagerAdapter(tokoNowView.getFragmentPage())
    }

    private val tabSelectedListener by lazy { createTabSelectedListener() }

    private val binding: ItemTokopedianowCategoryL2TabBinding? by viewBinding()

    init {
        binding?.apply {
            val pageChangeCallback = CategoryL2PageChangeCallback(viewPager, tokoNowView)
            viewPager.registerOnPageChangeCallback(pageChangeCallback)
            viewPager.adapter = viewPagerAdapter
        }
    }

    override fun bind(data: CategoryL2TabUiModel) {
        setupViewPager(data)
        setupTabMediator(data)
        setupTabLayout(data)
        setTabPosition(data)
    }

    override fun bind(data: CategoryL2TabUiModel, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true) {
            setupTabLayout(data)
            setTabPosition(data)
        }
    }

    private fun setupViewPager(data: CategoryL2TabUiModel) {
        data.categoryL2Ids.forEach {
            val fragment = TokoNowCategoryL2TabFragment
                .newInstance(data.categoryIdL1, it, data.componentList)
            viewPagerAdapter.addFragment(fragment)
        }
    }

    private fun setupTabMediator(data: CategoryL2TabUiModel) {
        binding?.apply {
            TabsUnifyMediator(tabUnify, viewPager) { tab, position ->
                val title = data.titleList.getOrNull(position).orEmpty()
                if (title.isNotBlank()) tab.setCustomText(title)
            }
        }
    }

    private fun setupTabLayout(data: CategoryL2TabUiModel) {
        binding?.tabUnify?.apply {
            val selectedTabPosition = data.selectedTabPosition
            tabLayout.removeOnTabSelectedListener(tabSelectedListener)
            tabLayout.getTabAt(selectedTabPosition)?.select()
            tabLayout.addOnTabSelectedListener(tabSelectedListener)
            customTabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun setTabPosition(data: CategoryL2TabUiModel) {
        val position = data.selectedTabPosition
        viewPagerAdapter.setSelectedTabPosition(position)
    }

    private fun createTabSelectedListener() = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            listener.onTabSelected(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
        }
    }

    fun loadMore() {
        viewPagerAdapter.loadMore()
    }

    fun onResume() {
        viewPagerAdapter.onResume()
    }

    interface CategoryL2TabListener {
        fun onTabSelected(position: Int)
    }
}
