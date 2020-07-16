package com.tokopedia.deals.brand.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.brand.ui.adapter.viewholder.DealsBrandViewHolder
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView

class  DealsBrandAdapterDelegate(
        val listener: DealsBrandActionListener
) : TypedAdapterDelegate<DealsBrandsDataView, Any, DealsBrandViewHolder>(DealsBrandViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: DealsBrandsDataView, holder: DealsBrandViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsBrandViewHolder {
        return DealsBrandViewHolder(basicView, listener)
    }

}