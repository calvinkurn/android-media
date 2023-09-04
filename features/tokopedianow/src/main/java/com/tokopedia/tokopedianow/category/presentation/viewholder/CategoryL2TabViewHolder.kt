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
    private val tokoNowView: TokoNowView?
) : AbstractViewHolder<CategoryL2TabUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.item_tokopedianow_category_l2_tab
    }

    private var viewPagerAdapter: CategoryL2TabViewPagerAdapter? = null

    private val binding: ItemTokopedianowCategoryL2TabBinding? by viewBinding()

    init {
        tokoNowView?.let {
            binding?.apply {
                val pageChangeCallback = CategoryL2PageChangeCallback(viewPager, tokoNowView)
                viewPagerAdapter = CategoryL2TabViewPagerAdapter(tokoNowView.getFragmentPage())
                viewPager.registerOnPageChangeCallback(pageChangeCallback)
                viewPager.adapter = viewPagerAdapter
            }
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
        binding?.apply {
            data.categoryL2Ids.forEach {
                val fragment = TokoNowCategoryL2TabFragment
                    .newInstance(data.categoryIdL1, it, data.componentList)
                viewPagerAdapter?.addFragment(fragment)
            }
            viewPager.currentItem = data.selectedTabPosition
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
            tabLayout.post {
                tabLayout.getTabAt(selectedTabPosition)?.select()
            }
            customTabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun setTabPosition(data: CategoryL2TabUiModel) {
        val position = data.selectedTabPosition
        viewPagerAdapter?.setSelectedTabPosition(position)
    }
}
