package com.tokopedia.product.detail.postatc.view.component.loading

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemPostAtcLoadingBinding
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class LoadingDelegate : TypedAdapterDelegate<LoadingUiModel, PostAtcUiModel, LoadingViewHolder>(R.layout.item_post_atc_loading) {
    override fun onBindViewHolder(item: LoadingUiModel, holder: LoadingViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): LoadingViewHolder {
        val binding = ItemPostAtcLoadingBinding.bind(basicView)
        return LoadingViewHolder(binding)
    }
}
