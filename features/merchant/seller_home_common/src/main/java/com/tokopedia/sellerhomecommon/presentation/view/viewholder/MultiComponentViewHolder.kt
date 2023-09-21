package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.viewpager2.widget.ViewPager2
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
) : AbstractViewHolder<MultiComponentWidgetUiModel>(itemView) {

    private val binding by lazy { ShcMultiComponentWidgetBinding.bind(itemView) }
    private val currentAdapter by lazy { MultiComponentAdapter(listener) }
    private var onTabSelectedListener: OnTabSelectedListener? = null

    override fun bind(element: MultiComponentWidgetUiModel) {
        val data = element.data
        when {
            data == null || element.showLoadingState -> setOnLoadingState()
            data.error.isNotBlank() -> setOnErrorState(element)
            else -> setOnSuccessState(element)
        }
    }

    override fun bind(element: MultiComponentWidgetUiModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            if (payloads[0] == 123) {
                currentAdapter.updateEmployeeListItems(element.data?.tabs ?: listOf())
                binding.vpShcMultiComponent.setPageTransformer { page, position ->
                    updateHeightBasedOnContent(page, binding.vpShcMultiComponent)
                }
            }
        } else {
            super.bind(element, payloads)
        }
    }

    private fun updateHeightBasedOnContent(view: View, pager: ViewPager2) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)
            pager.layoutParams =
                (pager.layoutParams).also { lp ->
                    lp.height = view.measuredHeight
                }
            pager.invalidate()
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

        onTabSelectedListener = object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { selectedIndex ->
                    element.data?.selectedTabPosition = selectedIndex
                    binding.vpShcMultiComponent.currentItem = selectedIndex
                    element.data?.tabs?.getOrNull(selectedIndex)?.let {
                        listener.multiComponentTabSelected(it)
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

        element.data?.tabs?.let { tabs ->
            setTabs(
                tabs,
                element.data?.selectedTabPosition ?: 0
            )
        }

        binding.vpShcMultiComponent.isUserInputEnabled = false
        currentAdapter.updateEmployeeListItems(element.data?.tabs ?: listOf())
    }

    private fun selectTabInitialLoad(selectedTabPosition: Int) {
        binding.vpShcMultiComponent.setCurrentItem(selectedTabPosition, false)
        binding.tabsShcMultiComponent.tabLayout.getTabAt(selectedTabPosition)?.select()
    }

    private fun setTabs(tabList: List<MultiComponentTab>, selectedTabPosition: Int) {
        binding.tabsShcMultiComponent.tabLayout.removeAllTabs()
        tabList.forEach {
            binding.tabsShcMultiComponent.addNewTab(it.title)
        }
        selectTabInitialLoad(selectedTabPosition)

        binding.vpShcMultiComponent.run {
            adapter = currentAdapter
        }
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_component_widget
    }

    interface Listener {
        fun multiComponentTabSelected(tab: MultiComponentTab)
    }


}
