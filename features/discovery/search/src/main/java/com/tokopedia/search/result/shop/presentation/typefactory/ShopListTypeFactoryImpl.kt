package com.tokopedia.search.result.shop.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.shop.presentation.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.result.shop.presentation.viewholder.ShopCpmViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopEmptySearchViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopItemViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopTotalCountViewHolder

internal class ShopListTypeFactoryImpl(
        private val shopListener: ShopListener,
        private val emptyStateListener: EmptyStateListener,
        private val bannerAdsListener: BannerAdsListener
) : BaseAdapterTypeFactory(), ShopListTypeFactory {

    override fun type(shopCpmViewModel: ShopCpmViewModel): Int {
        return ShopCpmViewHolder.LAYOUT
    }

    override fun type(shopTotalCountViewModel: ShopTotalCountViewModel): Int {
        return ShopTotalCountViewHolder.LAYOUT
    }

    override fun type(shopItem: ShopViewModel.ShopItem): Int {
        return ShopItemViewHolder.LAYOUT
    }

    override fun type(shopEmptySearchViewModel: ShopEmptySearchViewModel): Int {
        return ShopEmptySearchViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ShopEmptySearchViewHolder.LAYOUT -> ShopEmptySearchViewHolder(view, emptyStateListener)
            ShopCpmViewHolder.LAYOUT -> ShopCpmViewHolder(view, bannerAdsListener)
            ShopTotalCountViewHolder.LAYOUT -> ShopTotalCountViewHolder(view)
            ShopItemViewHolder.LAYOUT -> ShopItemViewHolder(view, shopListener)
            else -> super.createViewHolder(view, type)
        }
    }
}