package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class SearchProductHandleProductImpressionTest: ProductListPresenterTestFixtures() {

    private val className = "SearchClassName"
    private val capturedProductItemViewModel = slot<ProductItemViewModel>()
    private var suggestedRelatedKeyword = ""
    private val suggestedRelatedKeywordSlot = slot<String>()

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

    private fun `When handle product impressed`(productItemViewModel: ProductItemViewModel?, adapterPosition: Int) {
        productListPresenter.onProductImpressed(productItemViewModel, adapterPosition)
    }

    private fun `Then verify view not doing anything`() {
        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductImpressed for Top Ads Product without showing BOE CoachMark`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.imageUrl = imageUrl
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = true
            it.topadsImpressionUrl = topAdsImpressionUrl
            it.topadsClickUrl = topAdsClickUrl
            it.position = 1
        }
        val firstProductPosition = 0

        `Configure should show coachmark`(false)
        `Given className from view`()

        `When handle product impressed`(productItemViewModel, firstProductPosition)

        `Then verify interaction for Top Ads product impression with no coachmark shown`(productItemViewModel)
    }

    private fun `Given className from view`() {
        every { productListView.className } returns className
    }

    private fun `Then verify interaction for Top Ads product impression with no coachmark shown`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    productItemViewModel.topadsImpressionUrl,
                    productItemViewModel.productID,
                    productItemViewModel.productName,
                    productItemViewModel.imageUrl,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )

            productListView.sendTopAdsGTMTrackingProductImpression(capture(capturedProductItemViewModel))
        }

        verify(exactly = 0) {
            productListView.showOnBoarding(any())
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductImpressed for organic Product without showing BOE CoachMark`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = false
        }
        val firstProductPosition = 0

        `Configure should show coachmark`(false)
        `Given className from view`()

        `When handle product impressed`(productItemViewModel, firstProductPosition)
        `When getting suggestedRelatedKeyword`()

        `Then verify interaction for product impression with no coach mark shown`(productItemViewModel)
        `Then verify relatedKeyword`()
    }

    private fun `When getting suggestedRelatedKeyword`() {
        productListPresenter.suggestedRelatedKeyword
    }

    private fun `Then verify interaction for product impression with no coach mark shown`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.sendProductImpressionTrackingEvent(productItemViewModel, capture(suggestedRelatedKeywordSlot), any())
        }

        verify(exactly = 0) {
            productListView.showOnBoarding(any())
        }

        confirmVerified(productListView)
    }

    private fun `Then verify relatedKeyword`() {
        suggestedRelatedKeyword shouldBe suggestedRelatedKeywordSlot.captured
    }

    @Test
    fun `Handle onProductImpressed for organic ads product without showing BOE CoachMark`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.imageUrl = imageUrl
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = false
            it.isOrganicAds = true
            it.topadsClickUrl = topAdsClickUrl
            it.topadsImpressionUrl = topAdsImpressionUrl
        }
        val firstProductPosition = 0

        `Configure should show coachmark`(false)
        `Given className from view`()

        `When handle product impressed`(productItemViewModel, firstProductPosition)
        `When getting suggestedRelatedKeyword`()

        `Then verify interaction for Organic Ads product impression with no coach mark shown`(productItemViewModel)
        `Then verify relatedKeyword`()
    }

    private fun `Then verify interaction for Organic Ads product impression with no coach mark shown`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    productItemViewModel.topadsImpressionUrl,
                    productItemViewModel.productID,
                    productItemViewModel.productName,
                    productItemViewModel.imageUrl,
                    SearchConstant.TopAdsComponent.ORGANIC_ADS
            )

            productListView.sendProductImpressionTrackingEvent(capture(capturedProductItemViewModel), capture(suggestedRelatedKeywordSlot), any())
        }

        verify(exactly = 0) {
            productListView.showOnBoarding(any())
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductImpressed for Top Ads Product and show BOE CoachMark`() {
        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.imageUrl = imageUrl
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = true
            it.topadsImpressionUrl = topAdsImpressionUrl
            it.topadsClickUrl = topAdsClickUrl
            it.position = 1
            it.labelGroupList = listOf(
                    LabelGroupViewModel(position = "fulfillment", title = "TokoCabang", type = "darkGrey", imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2021/2/18/6d2cc121-91b9-49bc-99c3-d57437ad64b7.png")
            )
        }
        val firstProductPosition = 0

        `Configure should show coachmark`(true)
        `Given className from view`()
        `Given view already load data`(searchProductModel)

        `When handle product impressed`(productItemViewModel, firstProductPosition)

        `Then verify interaction for Top Ads product impression with coachmark shown`(productItemViewModel, firstProductPosition)
    }

    private fun `Given view already load data`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        productListPresenter.loadData(mapOf())
    }

    private fun `Then verify interaction for Top Ads product impression with coachmark shown`(productItemViewModel: ProductItemViewModel, position: Int) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    productItemViewModel.topadsImpressionUrl,
                    productItemViewModel.productID,
                    productItemViewModel.productName,
                    productItemViewModel.imageUrl,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )

            productListView.sendTopAdsGTMTrackingProductImpression(capture(capturedProductItemViewModel))
            productListView.showOnBoarding(position)
        }
    }

    @Test
    fun `Handle onProductImpressed for organic Product and show BOE CoachMark`() {
        val searchProductModel = "searchproduct/common-response.json".jsonToObject<SearchProductModel>()
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = false
        }
        val firstProductPosition = 0

        `Configure should show coachmark`(true)
        `Given className from view`()
        `Given view already load data`(searchProductModel)

        `When handle product impressed`(productItemViewModel, firstProductPosition)
        `When getting suggestedRelatedKeyword`()

        `Then verify interaction for product impression with coach mark shown`(productItemViewModel, firstProductPosition)
        `Then verify relatedKeyword`()
    }

    private fun `Then verify interaction for product impression with coach mark shown`(productItemViewModel: ProductItemViewModel, position: Int) {
        verify {
            productListView.sendProductImpressionTrackingEvent(productItemViewModel, capture(suggestedRelatedKeywordSlot))
            productListView.showOnBoarding(position)
        }
    }
}