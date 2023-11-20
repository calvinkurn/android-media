package com.tokopedia.product.detail.postatc.view.component.addons

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemPostAtcAddonsBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class AddonsDelegate(
    private val callback: PostAtcCallback
) : TypedAdapterDelegate<AddonsUiModel, PostAtcUiModel, AddonsViewHolder>(R.layout.item_post_atc_addons) {
    override fun onBindViewHolder(item: AddonsUiModel, holder: AddonsViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): AddonsViewHolder {
        val binding = ItemPostAtcAddonsBinding.bind(basicView)
        return AddonsViewHolder(binding, callback)
    }
}
