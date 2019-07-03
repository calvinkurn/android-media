package com.tokopedia.tkpd.home.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.banklist.view.adapter.viewholder.BankAccountViewHolder
import com.tokopedia.settingbank.banklist.view.adapter.viewholder.EmptyBankAccountViewHolder
import com.tokopedia.tkpd.home.adapter.OnWishlistActionButtonClicked
import com.tokopedia.tkpd.home.adapter.viewholder.WishlistEmptyViewHolder
import com.tokopedia.tkpd.home.adapter.viewholder.WishlistProductListViewHolder
import com.tokopedia.tkpd.home.adapter.viewholder.WishlistTopAdsListViewHolder
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistEmptyViewModel
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistProductViewModel
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistTopAdsViewModel
import com.tokopedia.tkpd.home.presenter.WishListView
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistAdapterFactory(val buttonActionClick: OnWishlistActionButtonClicked, val wishListView: WishListView, val wishlistAnalytics: WishlistAnalytics) :
        BaseAdapterTypeFactory(), WishlistTypeFactory {

    override fun type(viewModel: WishlistProductViewModel): Int {
        return WishlistProductListViewHolder.LAYOUT
    }

    override fun type(viewModel: WishlistEmptyViewModel): Int {
        return WishlistEmptyViewHolder.LAYOUT
    }

    override fun type(viewModel: WishlistTopAdsViewModel): Int {
        return WishlistTopAdsListViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            WishlistProductListViewHolder.LAYOUT -> WishlistProductListViewHolder(view, wishlistAnalytics, wishListView)
            WishlistEmptyViewHolder.LAYOUT -> WishlistEmptyViewHolder(view, buttonActionClick)
            WishlistTopAdsListViewHolder.LAYOUT -> WishlistTopAdsListViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }
}
