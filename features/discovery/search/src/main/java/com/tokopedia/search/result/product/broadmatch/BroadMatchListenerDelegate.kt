package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.ProductCardLifecycleObserver
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.utils.FragmentProvider

class BroadMatchListenerDelegate(
    private val presenter: BroadMatchPresenter?,
    override val productCardLifecycleObserver: ProductCardLifecycleObserver?,
    searchParameterProvider: SearchParameterProvider,
    fragmentProvider: FragmentProvider,
) : BroadMatchListener,
    SearchParameterProvider by searchParameterProvider,
    FragmentProvider by fragmentProvider {

    override fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView) {
        presenter?.onBroadMatchItemImpressed(broadMatchItemDataView)
    }

    override fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView) {
        presenter?.onBroadMatchItemClick(broadMatchItemDataView)
    }

    override fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView) {
        presenter?.onBroadMatchImpressed(broadMatchDataView)
    }

    override fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView) {
        presenter?.onBroadMatchSeeMoreClick(broadMatchDataView)
    }

    override fun onBroadMatchViewAllCardClicked(broadMatchDataView: BroadMatchDataView) {
        presenter?.onBroadMatchViewAllCardClicked(broadMatchDataView)
    }

    override fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView) {
        showProductCardOptions(getFragment(), createProductCardOptionsModel(broadMatchItemDataView))
    }

    private fun createProductCardOptionsModel(item: BroadMatchItemDataView): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()

        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.hasSimilarSearch = true
        productCardOptionsModel.isWishlisted = item.isWishlisted
        productCardOptionsModel.keyword = getSearchParameter()?.getSearchQuery() ?: ""
        productCardOptionsModel.productId = item.id
        productCardOptionsModel.screenName = SearchEventTracking.Category.SEARCH_RESULT
        productCardOptionsModel.seeSimilarProductEvent = SearchTracking.EVENT_CLICK_SEARCH_RESULT
        productCardOptionsModel.isTopAds = item.isOrganicAds
        productCardOptionsModel.topAdsWishlistUrl = item.topAdsWishlistUrl
        productCardOptionsModel.topAdsClickUrl = item.topAdsClickUrl

        return productCardOptionsModel
    }
}
