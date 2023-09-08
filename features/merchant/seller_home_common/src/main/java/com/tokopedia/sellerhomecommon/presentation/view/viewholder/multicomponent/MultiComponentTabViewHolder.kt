package com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.databinding.ShcMultiComponentViewBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MultiComponentAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.view.adapter.MultiComponentTabAdapter
import com.tokopedia.utils.view.binding.viewBinding

class MultiComponentTabViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var viewBinding: ShcMultiComponentViewBinding? by viewBinding()

    private val tabAdapter by lazy {
        MultiComponentTabAdapter(MultiComponentAdapterFactoryImpl())
    }

    fun bind(tab: MultiComponentTab) {
        if (tab.isLoaded) {
            if (tab.isError) {

            } else {
                viewBinding?.rvShcMultiComponentView?.gone()
                viewBinding?.rvShcMultiComponentView?.run {
                    visible()
                    adapter = tabAdapter
                    context?.let {
                        layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
                    }
                    isNestedScrollingEnabled = false
                }


                val componentData = tab.components.flatMap { it.data.orEmpty() }

                tabAdapter.setData(componentData)
            }
        } else {
            viewBinding?.rvShcMultiComponentView?.gone()
            viewBinding?.loaderShcMultiComponentView?.visible()

        }
    }

}


