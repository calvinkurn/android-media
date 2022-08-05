package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val searchProductWithTopAdsResponseJSON = "searchproduct/with-topads.json"

internal class SearchProductHandleProductExternalReferenceTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val className = "SearchClassName"
    private var externalReference = ""

    private fun `Configure should show coachmark`(
            shouldShow: Boolean
    ) {
        every { searchCoachMarkLocalCache.shouldShowBoeCoachmark() } answers { shouldShow }
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

        productListPresenter.loadData(mapOf(
            SearchApiConst.SRP_EXT_REF to externalReference
        ))
    }

    private fun findProductItemFromVisitableList(isTopAds: Boolean = false, isOrganicAds: Boolean = false): ProductItemDataView {
        val visitableList = visitableListSlot.captured

        return visitableList.find { it is ProductItemDataView && it.isTopAds == isTopAds && it.isOrganicAds == isOrganicAds } as ProductItemDataView
    }

    private fun `Then verify externalReference`(actualExternalReference: String) {
        actualExternalReference shouldBe externalReference
    }

    @Test
    fun `Organic Product with external Reference`() {
        externalReference = "1234567"

        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = false)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }

    @Test
    fun `Organic Product without external Reference`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = false)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }

    @Test
    fun `Organic Ads with external Reference`() {
        externalReference = "1234567"

        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = true)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }

    @Test
    fun `Organic Ads without external Reference`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = true)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }

    @Test
    fun `Top Ads with external Reference`() {
        externalReference = "1234567"

        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = true, isOrganicAds = false)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }

    @Test
    fun `Top Ads without external Reference`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON, false)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = true, isOrganicAds = false)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }
}