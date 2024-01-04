package com.tokopedia.deals.ui.category.ui.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.listener.EmptyStateListener
import com.tokopedia.deals.common.listener.ProductCardListener
import com.tokopedia.deals.common.ui.adapter.delegate.DealsCommonBrandGridAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.delegate.DealsProductCategoryAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.delegate.LoadingMoreUnifyAdapterDelegate
import com.tokopedia.deals.ui.brand.adapter.delegate.DealsBrandEmptyAdapterDelegate

/**
 * @author by firman on 16/06/20
 */

class DealsCategoryAdapter(
        brandActionListener: DealsBrandActionListener,
        productCardListener: ProductCardListener,
        emptyStateListener: EmptyStateListener
) : BaseCommonAdapter() {
    init {
        delegatesManager
                .addDelegate(DealsCommonBrandGridAdapterDelegate(brandActionListener))
                .addDelegate(DealsProductCategoryAdapterDelegate(productCardListener))
                .addDelegate(DealsBrandEmptyAdapterDelegate(emptyStateListener))
                .addDelegate(LoadingMoreUnifyAdapterDelegate())
    }
}
