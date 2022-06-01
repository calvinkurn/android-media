package com.tokopedia.search.result.shop.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.common.SearchLoadingMoreViewHolder
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.shop.chooseaddress.ChooseAddressDataView
import com.tokopedia.search.result.shop.chooseaddress.ChooseAddressListener
import com.tokopedia.search.result.shop.chooseaddress.ChooseAddressViewHolder
import com.tokopedia.search.result.shop.presentation.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.model.ShopCpmDataView
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchDataView
import com.tokopedia.search.result.shop.presentation.model.ShopRecommendationTitleDataView
import com.tokopedia.search.result.shop.presentation.model.ShopSuggestionDataView
import com.tokopedia.search.result.shop.presentation.model.ShopDataView
import com.tokopedia.search.result.shop.presentation.viewholder.ShopCpmViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopEmptySearchViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopItemViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopRecommendationTitleViewHolder
import com.tokopedia.search.result.shop.presentation.viewholder.ShopSuggestionViewHolder

internal class ShopListTypeFactoryImpl(
    private val shopListener: ShopListener,
    private val emptyStateListener: EmptyStateListener,
    private val bannerAdsListener: BannerAdsListener,
    private val chooseAddressListener: ChooseAddressListener,
) : BaseAdapterTypeFactory(), ShopListTypeFactory {

    override fun type(shopCpmDataView: ShopCpmDataView): Int {
        return ShopCpmViewHolder.LAYOUT
    }

    override fun type(shopDataItem: ShopDataView.ShopItem): Int {
        return ShopItemViewHolder.LAYOUT
    }

    override fun type(shopEmptySearchDataView: ShopEmptySearchDataView): Int {
        return ShopEmptySearchViewHolder.LAYOUT
    }

    override fun type(shopRecommendationTitleDataView: ShopRecommendationTitleDataView): Int {
        return ShopRecommendationTitleViewHolder.LAYOUT
    }

    override fun type(shopSuggestionDataView: ShopSuggestionDataView): Int {
        return ShopSuggestionViewHolder.LAYOUT
    }

    override fun type(chooseAddressDataView: ChooseAddressDataView): Int {
        return ChooseAddressViewHolder.LAYOUT
    }

    override fun type(loadingMoreModel: LoadingMoreModel): Int {
        return SearchLoadingMoreViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ShopEmptySearchViewHolder.LAYOUT -> ShopEmptySearchViewHolder(view, emptyStateListener)
            ShopCpmViewHolder.LAYOUT -> ShopCpmViewHolder(view, bannerAdsListener)
            ShopItemViewHolder.LAYOUT -> ShopItemViewHolder(view, shopListener)
            ShopRecommendationTitleViewHolder.LAYOUT -> ShopRecommendationTitleViewHolder(view)
            SearchLoadingMoreViewHolder.LAYOUT -> SearchLoadingMoreViewHolder(view)
            ShopSuggestionViewHolder.LAYOUT -> ShopSuggestionViewHolder(view)
            ChooseAddressViewHolder.LAYOUT -> ChooseAddressViewHolder(view, chooseAddressListener)
            else -> super.createViewHolder(view, type)
        }
    }
}