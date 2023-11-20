package com.tokopedia.product.detail.postatc.view.component.error

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemPostAtcErrorBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class ErrorDelegate(
    private val callback: PostAtcCallback
) : TypedAdapterDelegate<ErrorUiModel, PostAtcUiModel, ErrorViewHolder>(R.layout.item_post_atc_error) {
    override fun onBindViewHolder(item: ErrorUiModel, holder: ErrorViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ErrorViewHolder {
        val binding = ItemPostAtcErrorBinding.bind(basicView)
        return ErrorViewHolder(binding, callback)
    }
}
