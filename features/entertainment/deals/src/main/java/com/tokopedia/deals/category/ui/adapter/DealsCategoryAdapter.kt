package com.tokopedia.deals.category.ui.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.deals.brand.ui.adapter.delegate.DealsBrandEmptyAdapterDelegate
import com.tokopedia.deals.common.listener.DealChipsListActionListener
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.listener.EmptyStateListener
import com.tokopedia.deals.common.listener.ProductListListener
import com.tokopedia.deals.common.ui.adapter.delegate.DealsCommonBrandAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.delegate.DealsCommonBrandGridAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.delegate.DealsCommonChipsAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.delegate.DealsCommonProductAdapterDelegate

/**
 * @author by firman on 16/06/20
 */

class DealsCategoryAdapter(
    brandActionListener: DealsBrandActionListener,
    productListListener: ProductListListener,
    chipsActionListener: DealChipsListActionListener,
    emptyStateListener: EmptyStateListener
) : BaseCommonAdapter() {
    init {
        delegatesManager
            .addDelegate(DealsCommonChipsAdapterDelegate(chipsActionListener))
            .addDelegate(DealsCommonBrandGridAdapterDelegate(brandActionListener))
            .addDelegate(DealsCommonProductAdapterDelegate(productListListener))
            .addDelegate(DealsBrandEmptyAdapterDelegate(emptyStateListener))
    }
}