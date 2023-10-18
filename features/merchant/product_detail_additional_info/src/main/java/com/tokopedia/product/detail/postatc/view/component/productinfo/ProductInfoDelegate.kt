package com.tokopedia.product.detail.postatc.view.component.productinfo

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemProductInfoBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class ProductInfoDelegate(
    private val callback: PostAtcCallback
) : TypedAdapterDelegate<ProductInfoUiModel, PostAtcUiModel, ProductInfoViewHolder>(R.layout.item_product_info) {
    override fun onBindViewHolder(item: ProductInfoUiModel, holder: ProductInfoViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductInfoViewHolder {
        val binding = ItemProductInfoBinding.bind(basicView)
        return ProductInfoViewHolder(binding, callback)
    }
}
