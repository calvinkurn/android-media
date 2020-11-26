package com.tokopedia.deals.home.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.home.listener.DealsBannerActionListener
import com.tokopedia.deals.home.ui.adapter.viewholder.DealsBannersViewHolder
import com.tokopedia.deals.home.ui.dataview.BannersDataView

/**
 * @author by jessica on 16/06/20
 */

class DealsBannerAdapterDelegate(private val listener: DealsBannerActionListener):
        TypedAdapterDelegate<BannersDataView, Any, DealsBannersViewHolder>(DealsBannersViewHolder.LAYOUT)  {

    override fun onBindViewHolder(item: BannersDataView, holder: DealsBannersViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsBannersViewHolder {
        return DealsBannersViewHolder(basicView, listener)
    }
}