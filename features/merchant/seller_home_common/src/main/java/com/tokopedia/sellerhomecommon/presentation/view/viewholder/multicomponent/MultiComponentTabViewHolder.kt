package com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.databinding.ShcMultiComponentViewBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.view.adapter.MultiComponentTabAdapter
import com.tokopedia.utils.view.binding.viewBinding

class MultiComponentTabViewHolder(
    itemView: View,
    private val listener: WidgetListener,
) : RecyclerView.ViewHolder(itemView) {

    private var viewBinding: ShcMultiComponentViewBinding? by viewBinding()

    private val tabAdapter by lazy {
        MultiComponentTabAdapter(WidgetAdapterFactoryImpl(listener))
    }

    fun bind(tab: MultiComponentTab, widgetType: String) {
        if (!tab.isLoaded) {
            loadingTab()
            return
        }

        if (tab.isError) {
            showErrorTab(tab, widgetType)
        } else {
            loadSuccess(tab)
        }
    }

    private fun loadSuccess(tab: MultiComponentTab) = viewBinding?.run {
        loaderShcMultiComponentView.gone()
        shcMultiComponentErrorState.gone()

        rvShcMultiComponentView.run {
            visible()
            adapter = tabAdapter
            context?.let {
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }

            setupViewPool(true)
            isNestedScrollingEnabled = false
        }

        tabAdapter.setData(tab.components.map { it.data })

        itemView.addOnImpressionListener(tab.impressHolder) {
            listener.impressComponentDetailTab()
        }
    }

    private fun showErrorTab(tab: MultiComponentTab, widgetType: String) = viewBinding?.run {
        shcMultiComponentErrorState.setOnReloadClicked {
            listener.onReloadWidgetMultiComponent(tab, widgetType)
        }
        shcMultiComponentErrorState.show()
        rvShcMultiComponentView.gone()
        loaderShcMultiComponentView.gone()
    }

    private fun loadingTab() = viewBinding?.run {
        rvShcMultiComponentView.gone()
        shcMultiComponentErrorState.gone()
        loaderShcMultiComponentView.visible()
    }

    // Hanselable purpose
    private fun setupViewPool(activate: Boolean) {
        if (activate) {
            viewBinding?.rvShcMultiComponentView?.run {
                listener.getRvViewPool()?.let {
                    setRecycledViewPool(it)
                }
                (layoutManager as? LinearLayoutManager)?.recycleChildrenOnDetach = true
            }
        }
    }
}
