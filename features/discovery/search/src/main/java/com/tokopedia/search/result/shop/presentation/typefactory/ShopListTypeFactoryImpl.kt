package com.tokopedia.search.result.shop.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.EmptySearchViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopHeaderViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopItemViewHolder
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.presentation.view.listener.ShopListener
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactoryImpl
import com.tokopedia.search.result.shop.presentation.model.EmptySearchViewModel

class ShopListTypeFactoryImpl(
        private val shopListener: ShopListener,
        private val emptyStateListener: EmptyStateListener,
        private val bannerAdsListener: BannerAdsListener
) : SearchSectionTypeFactoryImpl(), ShopListTypeFactory {

    override fun type(emptySearchViewModel: com.tokopedia.search.result.presentation.model.EmptySearchViewModel?): Int {
        return EmptySearchViewHolder.LAYOUT
    }

    override fun type(shopHeader: ShopHeaderViewModel): Int {
        return ShopHeaderViewHolder.LAYOUT
    }

    override fun type(shopItem: ShopViewModel.ShopItem): Int {
        return ShopItemViewHolder.LAYOUT
    }

    override fun type(emptySearchViewModel: EmptySearchViewModel): Int {
        return EmptySearchViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            EmptySearchViewHolder.LAYOUT -> EmptySearchViewHolder(view, emptyStateListener, bannerAdsListener, null)
            ShopHeaderViewHolder.LAYOUT -> ShopHeaderViewHolder(view, bannerAdsListener)
            ShopItemViewHolder.LAYOUT -> ShopItemViewHolder(view, shopListener)
            else -> super.createViewHolder(view, type)
        }
    }
}