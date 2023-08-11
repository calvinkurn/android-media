package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.utils.FragmentProvider

class InspirationProductListenerDelegate(
    private val presenter: InspirationProductPresenter?,
    searchParameterProvider: SearchParameterProvider,
    fragmentProvider: FragmentProvider,
) : InspirationProductListener,
    SearchParameterProvider by searchParameterProvider,
    FragmentProvider by fragmentProvider {

    override fun onInspirationProductItemImpressed(inspirationProductData: InspirationProductItemDataView) {
        presenter?.onInspirationProductItemImpressed(inspirationProductData)
    }

    override fun onInspirationProductItemClicked(inspirationProductData: InspirationProductItemDataView) {
        presenter?.onInspirationProductItemClick(inspirationProductData)
    }

    override fun onInspirationProductItemThreeDotsClicked(inspirationProductData: InspirationProductItemDataView) {
        showProductCardOptions(getFragment(), createProductCardOptionsModel(inspirationProductData))
    }

    private fun createProductCardOptionsModel(item: InspirationProductItemDataView): ProductCardOptionsModel {
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
