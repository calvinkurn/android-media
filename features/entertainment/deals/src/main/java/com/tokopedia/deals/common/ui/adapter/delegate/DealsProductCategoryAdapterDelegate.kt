package com.tokopedia.deals.common.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.listener.ProductCardListener
import com.tokopedia.deals.common.ui.adapter.viewholder.ProductCardViewHolder
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener

class DealsProductCategoryAdapterDelegate(val listener: ProductCardListener)
    : TypedAdapterDelegate<ProductCardDataView, Any, ProductCardViewHolder>(ProductCardViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProductCardDataView, holder: ProductCardViewHolder) {
        holder.bindData(item)
        holder.itemView.addOnImpressionListener(item) {
            listener.onImpressionProduct(item, item.position, item.page)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductCardViewHolder {
        return ProductCardViewHolder(basicView, listener)
    }

}