package com.tokopedia.deals.common.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.listener.DealsTabsListener
import com.tokopedia.deals.common.ui.adapter.viewholder.DealsTabsViewHolder
import com.tokopedia.deals.common.ui.dataview.DealsTabsDataView

class DealsCommonTabsAdapterDelegate (
        val listener: DealsTabsListener
)        : TypedAdapterDelegate<DealsTabsDataView, Any, DealsTabsViewHolder>(DealsTabsViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: DealsTabsDataView, holder: DealsTabsViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsTabsViewHolder {
        return DealsTabsViewHolder(basicView, listener)
    }

}