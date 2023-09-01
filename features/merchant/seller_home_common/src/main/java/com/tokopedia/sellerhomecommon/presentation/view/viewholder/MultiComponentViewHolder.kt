package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcMultiComponentWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.MultiComponentAdapter

class MultiComponentViewHolder(
    itemView: View,
): AbstractViewHolder<MultiComponentWidgetUiModel>(itemView) {

    private val binding by lazy { ShcMultiComponentWidgetBinding.bind(itemView) }

    override fun bind(element: MultiComponentWidgetUiModel) {
        element.data?.tabs?.let { tabs ->
            setTabs(tabs)
        }
        binding.tabsShcMultiComponent.tabLayout.addOnTabSelectedListener(
            object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        binding.vpShcMultiComponent.currentItem = it
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            }
        )
        binding.vpShcMultiComponent.isUserInputEnabled = false
    }

    private fun setTabs(tabList: List<MultiComponentTab>) {
        binding.tabsShcMultiComponent.tabLayout.removeAllTabs()
        tabList.forEach {
            binding.tabsShcMultiComponent.addNewTab(it.title)
        }
        binding.vpShcMultiComponent.run {
            adapter = MultiComponentAdapter(tabList)
        }
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_component_widget
    }


}
