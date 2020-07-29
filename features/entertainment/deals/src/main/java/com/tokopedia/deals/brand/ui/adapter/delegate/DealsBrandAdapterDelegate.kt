package com.tokopedia.deals.brand.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.brand.ui.adapter.viewholder.DealsBrandOnlyViewHolder
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView

class DealsBrandAdapterDelegate (val listener: DealsBrandActionListener) :
        TypedAdapterDelegate<DealsBrandsDataView, Any, DealsBrandOnlyViewHolder>(
                DealsBrandOnlyViewHolder.LAYOUT
        ) {

    override fun onBindViewHolder(item: DealsBrandsDataView, holder: DealsBrandOnlyViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsBrandOnlyViewHolder {
        return DealsBrandOnlyViewHolder(basicView, listener)
    }
}