package com.tokopedia.deals.ui.brand.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.listener.EmptyStateListener
import com.tokopedia.deals.ui.brand.adapter.viewholder.DealsEmptyViewHolder
import com.tokopedia.deals.ui.brand.model.DealsEmptyDataView

class DealsBrandEmptyAdapterDelegate(
        private val emptyStateListener: EmptyStateListener
        ) : TypedAdapterDelegate<DealsEmptyDataView, Any, DealsEmptyViewHolder>(DealsEmptyViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: DealsEmptyDataView, holder: DealsEmptyViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsEmptyViewHolder {
        return DealsEmptyViewHolder(emptyStateListener,basicView)
    }
}
