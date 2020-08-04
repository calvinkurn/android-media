package com.tokopedia.deals.common.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.viewholder.ProductListViewHolder
import com.tokopedia.deals.category.ui.dataview.ProductListDataView
import com.tokopedia.deals.common.listener.ProductListListener

class DealsCommonProductAdapterDelegate(val listener: ProductListListener)
    : TypedAdapterDelegate<ProductListDataView, Any, ProductListViewHolder>(ProductListViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProductListDataView, holder: ProductListViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductListViewHolder {
        return ProductListViewHolder(basicView, listener)
    }

}