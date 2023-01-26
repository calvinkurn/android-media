package com.tokopedia.product.detail.postatc.component.error

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemErrorBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class ErrorDelegate(
    private val listener: PostAtcListener
) : TypedAdapterDelegate<ErrorUiModel, PostAtcUiModel, ErrorViewHolder>(R.layout.item_post_atc_error) {
    override fun onBindViewHolder(item: ErrorUiModel, holder: ErrorViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ErrorViewHolder {
        val binding = ItemErrorBinding.bind(basicView)
        return ErrorViewHolder(binding, listener)
    }

}
