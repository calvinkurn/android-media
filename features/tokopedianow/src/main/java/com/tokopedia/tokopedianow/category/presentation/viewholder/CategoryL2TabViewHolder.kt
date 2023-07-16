package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.fragment.DemoObjectFragment.DemoCollectionAdapter
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
): AbstractViewHolder<CategoryL2TabUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.item_tokopedianow_category_l2_tab
    }

    private val binding: ItemTokopedianowCategoryL2TabBinding? by viewBinding()

    override fun bind(data: CategoryL2TabUiModel) {
        binding?.apply {
            val demoCollectionAdapter = DemoCollectionAdapter(tokoNowView.getFragmentPage())
            viewPager.adapter = demoCollectionAdapter

            TabsUnifyMediator(tabUnify, viewPager) { tab, position ->
                data.tabTitleList.getOrNull(position)?.let {
                    tab.setCustomText(it)
                }
            }

            tabUnify.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    listener.onTabSelected(tab.position)
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {
                }
                override fun onTabReselected(tab: TabLayout.Tab) {
                }
            })
        }
    }

    interface CategoryL2TabListener {
        fun onTabSelected(position: Int)
    }
}
