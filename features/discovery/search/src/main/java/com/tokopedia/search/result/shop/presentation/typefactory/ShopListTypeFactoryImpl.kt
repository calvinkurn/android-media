package com.tokopedia.search.result.shop.presentation.typefactory

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.shop.presentation.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.model.*
import com.tokopedia.search.result.shop.presentation.viewholder.*

internal class ShopListTypeFactoryImpl(
        private val shopItemRecycledViewPool: RecyclerView.RecycledViewPool,
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
            ShopItemViewHolder.LAYOUT -> ShopItemViewHolder(view, shopItemRecycledViewPool, shopListener)
            else -> super.createViewHolder(view, type)
        }
    }
}