package com.tokopedia.sellerhomecommon.presentation.view.fragment

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcMultiComponentViewBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MultiComponentAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.view.adapter.MultiComponentTabAdapter
import com.tokopedia.utils.view.binding.viewBinding

class MultiComponentTabViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), MultiComponentTabFragmentInterface {

    private var viewBinding: ShcMultiComponentViewBinding? by viewBinding()

    override fun onSetData(tab: MultiComponentTab) {
        if (tab.isLoaded) {
            if (tab.isError) {
                // TODO: Populate error
            }
        }
    }

    fun bind(tab: MultiComponentTab) {
        viewBinding?.rvShcMultiComponentView?.run {
            adapter = MultiComponentTabAdapter(MultiComponentAdapterFactoryImpl())
        }
    }

}

interface MultiComponentTabFragmentInterface {

    fun onSetData(tab: MultiComponentTab)

}


