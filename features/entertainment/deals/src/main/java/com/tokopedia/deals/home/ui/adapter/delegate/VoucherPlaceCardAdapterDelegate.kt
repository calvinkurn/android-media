package com.tokopedia.deals.home.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.home.listener.DealsVoucherPlaceCardListener
import com.tokopedia.deals.home.ui.adapter.viewholder.VoucherPlacePopularViewHolder
import com.tokopedia.deals.home.ui.dataview.VoucherPlacePopularDataView

/**
 * @author by jessica on 16/06/20
 */

class VoucherPlaceCardAdapterDelegate(private val listener: DealsVoucherPlaceCardListener)
    : TypedAdapterDelegate<VoucherPlacePopularDataView, Any,
        VoucherPlacePopularViewHolder>(VoucherPlacePopularViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: VoucherPlacePopularDataView, holder: VoucherPlacePopularViewHolder) {
        holder.bindData(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): VoucherPlacePopularViewHolder {
        return VoucherPlacePopularViewHolder(basicView, listener)
    }

}