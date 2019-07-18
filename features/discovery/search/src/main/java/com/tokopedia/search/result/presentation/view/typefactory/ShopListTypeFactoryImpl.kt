package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.EmptySearchViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.shop.ShopItemViewHolder
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.presentation.view.listener.ShopListener

class ShopListTypeFactoryImpl(
        private val shopListener: ShopListener,
        private val emptyStateListener: EmptyStateListener,
        private val bannerAdsListener: BannerAdsListener
) : SearchSectionTypeFactoryImpl(), ShopListTypeFactory {

    override fun type(shopItem: ShopViewModel.ShopItem): Int {
        return ShopItemViewHolder.LAYOUT
    }

    override fun type(emptySearchModel: EmptySearchViewModel): Int {
        return EmptySearchViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>

        when (type) {
            EmptySearchViewHolder.LAYOUT -> viewHolder = EmptySearchViewHolder(view, emptyStateListener, bannerAdsListener, null)
            ShopItemViewHolder.LAYOUT -> viewHolder = ShopItemViewHolder(view, shopListener)
            else -> viewHolder = super.createViewHolder(view, type)
        }

        return viewHolder
    }
}