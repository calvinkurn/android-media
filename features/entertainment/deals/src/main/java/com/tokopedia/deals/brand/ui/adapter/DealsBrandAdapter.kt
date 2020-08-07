package com.tokopedia.deals.brand.ui.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.deals.brand.ui.adapter.delegate.DealsBrandAdapterDelegate
import com.tokopedia.deals.brand.ui.adapter.delegate.DealsBrandEmptyAdapterDelegate
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.listener.EmptyStateListener
import com.tokopedia.deals.common.ui.adapter.delegate.DealsCommonBrandGridAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.delegate.LoadingMoreUnifyAdapterDelegate
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView

class DealsBrandAdapter(
        listener: DealsBrandActionListener,
        emptyStateListener: EmptyStateListener
): BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(DealsBrandAdapterDelegate(listener))
                .addDelegate(DealsBrandEmptyAdapterDelegate(emptyStateListener))
                .addDelegate(LoadingMoreUnifyAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem.javaClass == newItem.javaClass) {
            if (oldItem is DealsBaseItemDataView && newItem is DealsBaseItemDataView) {
                return oldItem.javaClass == newItem.javaClass && oldItem.isLoaded == newItem.isLoaded && oldItem.isSuccess == newItem.isSuccess
            }
        }
        return false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }
}