package com.tokopedia.search.result.product.deduplication

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductV5
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.topads.sdk.domain.model.Product
import javax.inject.Inject

@SearchScope
class Deduplication @Inject constructor(
    private val deduplicationView: DeduplicationView,
) {

    private val productIdList = mutableListOf<String>()

    fun appendProductId(searchProductModel: SearchProductModel) {
        productIdList.addAll(cpmProductIdList(searchProductModel))
        productIdList.addAll(organicV4ProductIdList(searchProductModel))
        productIdList.addAll(organicV5ProductIdList(searchProductModel))
        productIdList.addAll(getTopAdsProductIdList(searchProductModel))
    }

    fun clear() { productIdList.clear() }

    private fun cpmProductIdList(searchProductModel: SearchProductModel): List<String> =
        searchProductModel.cpmModel.data.flatMap { cpmData ->
            cpmData.cpm.cpmShop.products.map(Product::id)
        }

    private fun organicV4ProductIdList(searchProductModel: SearchProductModel) =
        searchProductModel.searchProduct.data.productList.map(SearchProductModel.Product::id)

    private fun organicV5ProductIdList(searchProductModel: SearchProductModel) =
        searchProductModel.searchProductV5.data.productList.map(SearchProductV5.Data.Product::id)

    private fun getTopAdsProductIdList(searchProductModel: SearchProductModel) =
        searchProductModel.topAdsModel.data.map { it.product.id }

    fun removeDuplicate(productList: List<InspirationCarouselDataView.Option.Product>) =
        productList.filter { product -> !productIdList.contains(product.id) }

    fun isCarouselWithinThreshold(
        option: InspirationCarouselDataView.Option,
        productList: List<InspirationCarouselDataView.Option.Product>,
    ): Boolean {
        val isWithinThreshold = productList.size >= MINIMUM_CAROUSEL_PRODUCT_COUNT

        if (!isWithinThreshold)
            deduplicationView.trackRemoved(
                option.componentId,
                option.applink,
                option.externalReference,
            )

        return isWithinThreshold
    }

    companion object {
        const val MINIMUM_CAROUSEL_PRODUCT_COUNT = 2
    }
}
