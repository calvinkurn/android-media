package com.tokopedia.product.detail.postatc.component.productinfo

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.R

class ProductInfoDelegate : TypedAdapterDelegate<ProductInfoUiModel, Any, ProductInfoViewHolder>(R.layout.item_product_info) {
    override fun onBindViewHolder(item: ProductInfoUiModel, holder: ProductInfoViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductInfoViewHolder {
        return ProductInfoViewHolder(basicView)
    }
}
