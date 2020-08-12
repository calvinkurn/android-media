package com.tokopedia.deals.common.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.viewholder.DealsBrandGridViewHolder
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView

class DealsCommonBrandGridAdapterDelegate(val listener: DealsBrandActionListener) :
        TypedAdapterDelegate<DealsBrandsDataView, Any, DealsBrandGridViewHolder>(
                DealsBrandGridViewHolder.LAYOUT
        ) {

    override fun onBindViewHolder(item: DealsBrandsDataView, holder: DealsBrandGridViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsBrandGridViewHolder {
        return DealsBrandGridViewHolder(basicView, listener)
    }
}