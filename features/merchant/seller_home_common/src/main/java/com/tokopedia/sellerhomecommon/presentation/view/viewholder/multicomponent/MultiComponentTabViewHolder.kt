package com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private val listener: WidgetListener
) : RecyclerView.ViewHolder(itemView) {

    private var viewBinding: ShcMultiComponentViewBinding? by viewBinding()

    private val tabAdapter by lazy {
        MultiComponentTabAdapter(WidgetAdapterFactoryImpl(listener))
    }

    fun bind(tab: MultiComponentTab) {
        if (tab.isLoaded) {
            if (tab.isError) {
                viewBinding?.txtShcMultiComponentView?.show()
                viewBinding?.rvShcMultiComponentView?.gone()
                viewBinding?.loaderShcMultiComponentView?.gone()
            } else {
                viewBinding?.loaderShcMultiComponentView?.gone()
                viewBinding?.txtShcMultiComponentView?.gone()
                viewBinding?.rvShcMultiComponentView?.gone()
                viewBinding?.rvShcMultiComponentView?.run {
                    visible()
                    adapter = tabAdapter
                    context?.let {
                        layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
                    }

                    setupViewPool(true)
                    isNestedScrollingEnabled = false
                }

                tabAdapter.setData(tab.components.map { it.data })
            }
        } else {
            viewBinding?.rvShcMultiComponentView?.gone()
            viewBinding?.loaderShcMultiComponentView?.visible()
        }
    }

    // Hanselable purpose
    private fun setupViewPool(activate: Boolean) {
        if (activate) {
            viewBinding?.rvShcMultiComponentView?.run {
                listener.getRvViewPool()?.let {
                    setRecycledViewPool(it)
                }
                (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
            }
        }
    }
}
