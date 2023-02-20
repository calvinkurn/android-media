package com.tokopedia.product.detail.postatc.component.productinfo

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemProductInfoBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class ProductInfoDelegate(
    val listener: PostAtcListener
) : TypedAdapterDelegate<ProductInfoUiModel, PostAtcUiModel, ProductInfoViewHolder>(R.layout.item_product_info) {
    override fun onBindViewHolder(item: ProductInfoUiModel, holder: ProductInfoViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductInfoViewHolder {
        val binding = ItemProductInfoBinding.bind(basicView)
        return ProductInfoViewHolder(binding, listener)
    }
}
