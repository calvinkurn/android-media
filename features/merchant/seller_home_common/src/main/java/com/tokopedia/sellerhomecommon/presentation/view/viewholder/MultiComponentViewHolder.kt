package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.databinding.ShcMultiComponentWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.MultiComponentAdapter

class MultiComponentViewHolder(
    itemView: View,
    private val listener: WidgetListener
): AbstractViewHolder<MultiComponentWidgetUiModel>(itemView) {

    private val binding by lazy { ShcMultiComponentWidgetBinding.bind(itemView) }

    private var onTabSelectedListener: OnTabSelectedListener? = null

    override fun bind(element: MultiComponentWidgetUiModel) {
        val data = element.data
        when {
            data == null || element.showLoadingState -> setOnLoadingState()
            data.error.isNotBlank() -> setOnErrorState(element)
            else -> setOnSuccessState(element)
        }
    }

    private fun setOnLoadingState() {
        binding.tabsShcMultiComponent.gone()
        binding.vpShcMultiComponent.gone()
        binding.loaderShcMultiComponent.visible()
    }

    private fun setOnErrorState(element: MultiComponentWidgetUiModel) {
        // TODO
    }

    private fun setOnSuccessState(element: MultiComponentWidgetUiModel) {
        binding.tabsShcMultiComponent.visible()
        binding.vpShcMultiComponent.visible()
        binding.loaderShcMultiComponent.gone()

        onTabSelectedListener?.let {
            binding.tabsShcMultiComponent.tabLayout.removeOnTabSelectedListener(it)
        }

        element.data?.tabs?.let { tabs ->
            setTabs(tabs)
        }

        onTabSelectedListener = object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { selectedIndex ->
                    binding.vpShcMultiComponent.currentItem = selectedIndex
                    element.data?.tabs?.forEachIndexed { index, multiComponentTab ->
                        val isSameIndex = selectedIndex == index
                        multiComponentTab.isSelected = isSameIndex

                        if (isSameIndex) {
                            listener.onTabSelected(multiComponentTab)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        }

        onTabSelectedListener?.let {
            binding.tabsShcMultiComponent.tabLayout.addOnTabSelectedListener(it)
        }

        val selectedItemIndex =
            element.data?.tabs?.indexOfFirst { it.isSelected } ?: RecyclerView.NO_POSITION

        if (selectedItemIndex > RecyclerView.NO_POSITION) {
            binding.tabsShcMultiComponent.tabLayout.setScrollPosition(selectedItemIndex, 0f, true)
            binding.vpShcMultiComponent.setCurrentItem(selectedItemIndex, false)
            binding.tabsShcMultiComponent.tabLayout.getTabAt(selectedItemIndex)?.select()
        } else {
            element.data?.tabs?.firstOrNull()?.let { tab ->
                tab.isSelected = true
                listener.onTabSelected(tab)
            }
        }

        binding.vpShcMultiComponent.isUserInputEnabled = false
    }

    private fun setTabs(tabList: List<MultiComponentTab>) {
        binding.tabsShcMultiComponent.tabLayout.removeAllTabs()
        tabList.forEach {
            binding.tabsShcMultiComponent.addNewTab(it.title)
        }
        binding.vpShcMultiComponent.run {
            adapter = MultiComponentAdapter(tabList.toMutableList())
        }

    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_component_widget
    }

    interface Listener {
        fun onTabSelected(tab: MultiComponentTab)
    }


}
