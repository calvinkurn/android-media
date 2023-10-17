package com.tokopedia.product.detail.postatc.view.component.fallback

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemPostAtcFallbackBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class FallbackDelegate(
    private val callback: PostAtcCallback
) : TypedAdapterDelegate<FallbackUiModel, PostAtcUiModel, FallbackViewHolder>(R.layout.item_post_atc_fallback) {
    override fun onBindViewHolder(item: FallbackUiModel, holder: FallbackViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): FallbackViewHolder {
        val binding = ItemPostAtcFallbackBinding.bind(basicView)
        return FallbackViewHolder(binding, callback)
    }
}
