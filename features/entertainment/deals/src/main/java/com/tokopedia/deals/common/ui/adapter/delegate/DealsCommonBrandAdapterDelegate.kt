package com.tokopedia.deals.common.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.viewholder.DealsBrandsViewHolder
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView

/**
 * @author by jessica on 17/06/20
 */

class DealsCommonBrandAdapterDelegate(val listener: DealsBrandActionListener)
    : TypedAdapterDelegate<DealsBrandsDataView, Any, DealsBrandsViewHolder>(DealsBrandsViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: DealsBrandsDataView, holder: DealsBrandsViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsBrandsViewHolder {
        return DealsBrandsViewHolder(basicView, listener)
    }

}