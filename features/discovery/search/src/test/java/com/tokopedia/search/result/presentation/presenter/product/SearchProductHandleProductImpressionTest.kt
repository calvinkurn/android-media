package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val searchProductWithTopAdsResponseJSON = "searchproduct/with-topads.json"

internal class SearchProductHandleProductImpressionTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val capturedProductItemViewModel = slot<ProductItemDataView>()

    @Test
    fun `Handle onProductImpressed with null ProductItemViewModel (degenerate cases)`() {
        val firstProductPosition = -1
        `Configure should show coachmark`(false)
        `When handle product impressed`(null, firstProductPosition)

        `Then verify view not doing anything`()
    }

    private fun `Configure should show coachmark`(
            shouldShow: Boolean
    ) {
        every { searchCoachMarkLocalCache.shouldShowBoeCoachmark() } answers { shouldShow }
    }

    private fun `When handle product impressed`(productItemDataView: ProductItemDataView?, adapterPosition: Int) {
        productListPresenter.onProductImpressed(productItemDataView, adapterPosition)
    }

    private fun `Then verify view not doing anything`() {
        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductImpressed for Top Ads Product without showing BOE CoachMark`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = true, isOrganicAds = false)
        val firstProductPosition = 0

        `When handle product impressed`(productItemViewModel, firstProductPosition)

        `Then verify interaction for Top Ads product impression with no coachmark shown`(productItemViewModel)
    }

    private fun `Given View already load data`(responseJSON: String, shouldShowCoachmark: Boolean = false) {
        val searchProductModel = responseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Configure should show coachmark`(shouldShowCoachmark)
        `Given className from view`()
        `Given view already load data`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given className from view`() {
        every { productListView.className } returns className
    }

    private fun `Given view already load data`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf())
    }

    private fun findProductItemFromVisitableList(isTopAds: Boolean = false, isOrganicAds: Boolean = false): ProductItemDataView {
        val visitableList = visitableListSlot.captured

        return visitableList.find { it is ProductItemDataView && it.isTopAds == isTopAds && it.isOrganicAds == isOrganicAds } as ProductItemDataView
    }

    private fun `Then verify interaction for Top Ads product impression with no coachmark shown`(productItemDataView: ProductItemDataView) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    productItemDataView.topadsImpressionUrl,
                    productItemDataView.productID,
                    productItemDataView.productName,
                    productItemDataView.imageUrl,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )

            productListView.sendTopAdsGTMTrackingProductImpression(productItemDataView)
        }

        verify(exactly = 0) {
            productListView.showOnBoarding(any())
        }
    }

    @Test
    fun `Handle onProductImpressed for organic Product without showing BOE CoachMark`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = false)
        val firstProductPosition = 0

        `When handle product impressed`(productItemViewModel, firstProductPosition)

        `Then verify interaction for product impression with no coach mark shown`(productItemViewModel)
    }

    private fun `Then verify interaction for product impression with no coach mark shown`(productItemDataView: ProductItemDataView) {
        verify {
            productListView.sendProductImpressionTrackingEvent(productItemDataView, any())
        }

        verify(exactly = 0) {
            productListView.showOnBoarding(any())
        }
    }

    @Test
    fun `Handle onProductImpressed for organic ads product without showing BOE CoachMark`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = true)
        val firstProductPosition = 0

        `When handle product impressed`(productItemViewModel, firstProductPosition)

        `Then verify interaction for Organic Ads product impression with no coach mark shown`(productItemViewModel)
    }

    private fun `Then verify interaction for Organic Ads product impression with no coach mark shown`(productItemDataView: ProductItemDataView) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    productItemDataView.topadsImpressionUrl,
                    productItemDataView.productID,
                    productItemDataView.productName,
                    productItemDataView.imageUrl,
                    SearchConstant.TopAdsComponent.ORGANIC_ADS
            )

            productListView.sendProductImpressionTrackingEvent(capture(capturedProductItemViewModel), any())
        }

        verify(exactly = 0) {
            productListView.showOnBoarding(any())
        }
    }

    @Test
    fun `Handle onProductImpressed for Top Ads Product and show BOE CoachMark`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON, true)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = true, isOrganicAds = false)
        val firstProductPosition = 1

        `When handle product impressed`(productItemViewModel, firstProductPosition)

        `Then verify interaction for Top Ads product impression with coachmark shown`(productItemViewModel, firstProductPosition)
    }

    private fun `Then verify interaction for Top Ads product impression with coachmark shown`(productItemDataView: ProductItemDataView, position: Int) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    productItemDataView.topadsImpressionUrl,
                    productItemDataView.productID,
                    productItemDataView.productName,
                    productItemDataView.imageUrl,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )

            productListView.sendTopAdsGTMTrackingProductImpression(capture(capturedProductItemViewModel))
            productListView.showOnBoarding(position)
        }
    }

    @Test
    fun `Handle onProductImpressed for organic Product and show BOE CoachMark`() {
        `Given View already load data`(searchProductCommonResponseJSON, true)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = false)
        val firstProductPosition = 1

        `When handle product impressed`(productItemViewModel, firstProductPosition)

        `Then verify interaction for product impression with coach mark shown`(productItemViewModel, firstProductPosition)
    }

    private fun `Then verify interaction for product impression with coach mark shown`(productItemDataView: ProductItemDataView, position: Int) {
        verify {
            productListView.sendProductImpressionTrackingEvent(productItemDataView, any())
            productListView.showOnBoarding(position)
        }
    }

    @Test
    fun `Handle onProductImpressed for organic Product with related keyword`() {
        val searchProductModel = "searchproduct/impressionclick/related.json".jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view already load data`()

        val productItem = findProductItemFromVisitableList(
            isTopAds = false,
            isOrganicAds = false,
        )
        val firstProductPosition = 1

        `When handle product impressed`(productItem, firstProductPosition)

        verify {
            productListView.sendProductImpressionTrackingEvent(
                productItem,
                searchProductModel.searchProduct.relatedKeyword,
            )
        }
    }

    @Test
    fun `Handle onProductImpressed for organic Product with suggestion keyword`() {
        val searchProductModel = "searchproduct/impressionclick/suggestion.json".jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view already load data`()

        val productItem = findProductItemFromVisitableList(
            isTopAds = false,
            isOrganicAds = false,
        )
        val firstProductPosition = 1

        `When handle product impressed`(productItem, firstProductPosition)

        verify {
            productListView.sendProductImpressionTrackingEvent(
                productItem,
                searchProductModel.searchProduct.data.suggestion.suggestion,
            )
        }
    }

    @Test
    fun `Handle onProductImpressed for organic Product with related keyword for reimagine`() {
        val searchProductModel = "searchproduct/impressionclick/related-reimagine.json"
            .jsonToObject<SearchProductModel>()
        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view already load data`()

        val productItem = findProductItemFromVisitableList(
            isTopAds = false,
            isOrganicAds = false,
        )
        val firstProductPosition = 1

        `When handle product impressed`(productItem, firstProductPosition)

        verify {
            productListView.sendProductImpressionTrackingEvent(
                productItem,
                searchProductModel.searchProductV5.data.related.relatedKeyword,
            )
        }
    }

    @Test
    fun `Handle onProductImpressed for organic Product with suggestion keyword for reimagine`() {
        val searchProductModel = "searchproduct/impressionclick/suggestion-reimagine.json"
            .jsonToObject<SearchProductModel>()
        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view already load data`()

        val productItem = findProductItemFromVisitableList(
            isTopAds = false,
            isOrganicAds = false,
        )
        val firstProductPosition = 1

        `When handle product impressed`(productItem, firstProductPosition)

        verify {
            productListView.sendProductImpressionTrackingEvent(
                productItem,
                searchProductModel.searchProductV5.data.suggestion.suggestion,
            )
        }
    }
}
