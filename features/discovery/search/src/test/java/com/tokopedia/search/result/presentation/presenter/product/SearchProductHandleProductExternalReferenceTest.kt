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

    private fun `Given View already load data`(
        responseJSON: String,
        externalReference: String = "",
    ) {
        val searchProductModel = responseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `When view load data`(externalReference)
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When view load data`(externalReference: String) {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf(
            SearchApiConst.SRP_EXT_REF to externalReference
        ))
    }

    private fun findProductItemFromVisitableList(isTopAds: Boolean = false, isOrganicAds: Boolean = false): ProductItemDataView {
        val visitableList = visitableListSlot.captured

        return visitableList.find { it is ProductItemDataView && it.isTopAds == isTopAds && it.isOrganicAds == isOrganicAds } as ProductItemDataView
    }

    private fun `Then verify externalReference`(
        actualExternalReference: String,
        expectedExternalReference: String = "",
    ) {
        actualExternalReference shouldBe expectedExternalReference
    }

    @Test
    fun `Organic Product with external Reference`() {
        val expectedExternalReference = "1234567"

        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = false)

        `Then verify externalReference`(productItemViewModel.dimension131, expectedExternalReference)
    }

    @Test
    fun `Organic Product without external Reference`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = false)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }

    @Test
    fun `Organic Ads with external Reference`() {
        val expectedExternalReference = "1234567"

        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = true)

        `Then verify externalReference`(productItemViewModel.dimension131, expectedExternalReference)
    }

    @Test
    fun `Organic Ads without external Reference`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = false, isOrganicAds = true)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }

    @Test
    fun `Top Ads with external Reference`() {
        val expectedExternalReference = "1234567"

        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = true, isOrganicAds = false)

        `Then verify externalReference`(productItemViewModel.dimension131, expectedExternalReference)
    }

    @Test
    fun `Top Ads without external Reference`() {
        `Given View already load data`(searchProductWithTopAdsResponseJSON)

        val productItemViewModel = findProductItemFromVisitableList(isTopAds = true, isOrganicAds = false)

        `Then verify externalReference`(productItemViewModel.dimension131)
    }
}