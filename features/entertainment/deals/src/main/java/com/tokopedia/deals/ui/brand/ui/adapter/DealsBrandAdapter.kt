package com.tokopedia.deals.ui.brand.ui.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.listener.EmptyStateListener
import com.tokopedia.deals.common.ui.adapter.delegate.LoadingMoreUnifyAdapterDelegate
import com.tokopedia.deals.ui.brand.ui.adapter.delegate.DealsBrandAdapterDelegate
import com.tokopedia.deals.ui.brand.ui.adapter.delegate.DealsBrandEmptyAdapterDelegate

class DealsBrandAdapter(
        listener: DealsBrandActionListener,
        emptyStateListener: EmptyStateListener
): BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(DealsBrandAdapterDelegate(listener))
                .addDelegate(DealsBrandEmptyAdapterDelegate(emptyStateListener))
                .addDelegate(LoadingMoreUnifyAdapterDelegate())
    }
}
