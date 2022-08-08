package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val broadMatchResponseCode0Page1Position1 = "searchproduct/broadmatch/response-code-0-page-1-position-1.json"
private const val dynamicProductCarousel = "searchproduct/inspirationcarousel/dynamic-product.json"

internal class SearchProductHandleBroadMatchExternalReferenceTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    private fun `Given View already load data with broad match`(
        searchProductModel: SearchProductModel,
        externalReference: String = ""
    ) {
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given view already load data`(externalReference)
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view already load data`(externalReference: String) {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        productListPresenter.loadData(mapOf(
            SearchApiConst.SRP_EXT_REF to externalReference
        ))
    }

    private fun findBroadMatchItemFromVisitableList(isTopAds: Boolean): BroadMatchItemDataView {
        val visitableList = visitableListSlot.captured

        val broadMatchViewModel = visitableList.find { it is BroadMatchDataView } as BroadMatchDataView

        return broadMatchViewModel.broadMatchItemDataViewList.find { it.isOrganicAds == isTopAds }!!
    }

    @Test
    fun `Top ads broad match item with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel, expectedExternalReference)

        val broadMatchAds = findBroadMatchItemFromVisitableList(true)

        `Then verify externalReference`(broadMatchAds.externalReference, expectedExternalReference)
    }

    @Test
    fun `Top ads broad match item without externalReference`() {
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchAds = findBroadMatchItemFromVisitableList(true)

        `Then verify externalReference`(broadMatchAds.externalReference)
    }

    @Test
    fun `Broad match item with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel, expectedExternalReference)

        val broadMatchNotAds = findBroadMatchItemFromVisitableList(false)

        `Then verify externalReference`(broadMatchNotAds.externalReference, expectedExternalReference)
    }

    @Test
    fun `Broad match item without externalReference`() {
        val searchProductModel = broadMatchResponseCode0Page1Position1.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val broadMatchNotAds = findBroadMatchItemFromVisitableList(false)

        `Then verify externalReference`(broadMatchNotAds.externalReference)
    }

    @Test
    fun `Dynamic carousel as broad match with externalReference`() {
        val expectedExternalReference = "1234567"
        val searchProductModel = dynamicProductCarousel.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel, expectedExternalReference)

        val dynamicProductCarousel = findBroadMatchItemFromVisitableList(false)

        `Then verify externalReference`(dynamicProductCarousel.externalReference, expectedExternalReference)
    }

    @Test
    fun `Dynamic carousel as broad match without externalReference`() {
        val searchProductModel = dynamicProductCarousel.jsonToObject<SearchProductModel>()
        `Given View already load data with broad match`(searchProductModel)

        val dynamicProductCarousel = findBroadMatchItemFromVisitableList(false)

        `Then verify externalReference`(dynamicProductCarousel.externalReference)
    }

    private fun `Then verify externalReference`(
        actualExternalReference: String,
        expectedExternalReference: String = "",
    ) {
        actualExternalReference shouldBe expectedExternalReference
    }
}