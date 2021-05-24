package com.tokopedia.deals.home.ui.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.deals.common.listener.CuratedProductCategoryListener
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.delegate.CuratedProductCategoryAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.delegate.DealsCommonBrandAdapterDelegate
import com.tokopedia.deals.common.ui.adapter.delegate.LoadingMoreUnifyAdapterDelegate
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.home.listener.DealsBannerActionListener
import com.tokopedia.deals.home.listener.DealsCategoryListener
import com.tokopedia.deals.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.home.listener.DealsVoucherPlaceCardListener
import com.tokopedia.deals.home.ui.adapter.delegate.*

/**
 * @author by jessica on 16/06/20
 */

class DealsHomeAdapter(dealsVoucherPlaceCardListener: DealsVoucherPlaceCardListener,
                       curatedProductCategoryListener: CuratedProductCategoryListener,
                       bannerActionListener: DealsBannerActionListener,
                       categoryListener: DealsCategoryListener,
                       brandActionListener: DealsBrandActionListener,
                       favouriteCategoriesActionListener: DealsFavouriteCategoriesListener) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(VoucherPlaceCardAdapterDelegate(dealsVoucherPlaceCardListener))
                .addDelegate(DealsBannerAdapterDelegate(bannerActionListener))
                .addDelegate(DealsTickerAdapterDelegate())
                .addDelegate(DealsCategoriesAdapterDelegate(categoryListener))
                .addDelegate(CuratedProductCategoryAdapterDelegate(curatedProductCategoryListener))
                .addDelegate(DealsCommonBrandAdapterDelegate(brandActionListener))
                .addDelegate(DealsFavouriteCategoriesAdapterDelegate(favouriteCategoriesActionListener))
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