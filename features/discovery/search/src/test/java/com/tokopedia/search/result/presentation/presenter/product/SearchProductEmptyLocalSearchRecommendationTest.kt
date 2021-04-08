package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.TestException
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.error
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val searchProductLocalSearchEmptyJSON = "searchproduct/localsearch/empty-search-response-code-11.json"
private const val emptyLocalSearchRecommendationPage1JSON = "searchproduct/localsearchrecommendation/empty-local-search-recommendation-page-1.json"

internal class SearchProductEmptyLocalSearchRecommendationTest : ProductListPresenterTestFixtures() {

    private val navsource = "clp"
    private val searchProductPageTitle = "Waktu Indonesia Belanja"
    private val searchProductPageId = "1234"
    private val keyword = "asus"
    private val searchParameter = mapOf(
            SearchApiConst.Q to keyword,
            SearchApiConst.NAVSOURCE to navsource,
            SearchApiConst.SRP_PAGE_TITLE to searchProductPageTitle,
            SearchApiConst.SRP_PAGE_ID to searchProductPageId
    )

    private val localSearchRecomRequestParamsSlot = mutableListOf<RequestParams>()
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Get empty local search recommendation success during local search - page 1`() {
        val searchProductModel = searchProductLocalSearchEmptyJSON.jsonToObject<SearchProductModel>()
        val localSearchRecommendationModel = emptyLocalSearchRecommendationPage1JSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given get local search recommendation will return SearchProductModel`(localSearchRecommendationModel)

        `When Load Data`(searchParameter)

        `Then verify request params for local search recommendation`(listOf(0))
        `Then verify view interaction for success get local search recommendation`(localSearchRecommendationModel)
        `Then verify has next page`(true)
    }

    private fun `Given get local search recommendation will return SearchProductModel`(
            localSearchRecommendationModel: SearchProductModel
    ) {
        every {
            getLocalSearchRecommendationUseCase.execute(capture(localSearchRecomRequestParamsSlot), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(localSearchRecommendationModel)
        }
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify request params for local search recommendation`(expectedStart: List<Int>) {
        verifyLocalSearchRecommendationParamsCapturedCount(expectedStart.size)

        localSearchRecomRequestParamsSlot.forEachIndexed { index, requestParams ->
            val parameters = requestParams.parameters

            parameters[SearchApiConst.SOURCE] shouldBe SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH
            parameters[SearchApiConst.DEVICE] shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
            parameters[SearchApiConst.NAVSOURCE] shouldBe navsource
            parameters[SearchApiConst.SRP_PAGE_TITLE] shouldBe searchProductPageTitle
            parameters[SearchApiConst.SRP_PAGE_ID] shouldBe searchProductPageId
            parameters[SearchApiConst.START] shouldBe expectedStart[index].toString()
            parameters[SearchApiConst.ROWS] shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS
        }
    }

    private fun verifyLocalSearchRecommendationParamsCapturedCount(expectedParamSizeCount: Int) {
        if (localSearchRecomRequestParamsSlot.size != expectedParamSizeCount) {
            val detailMessage =
                    "Local Search Recommendation use case is called ${localSearchRecomRequestParamsSlot.size} times, " +
                    "but expected $expectedParamSizeCount times."

            throw AssertionError(detailMessage)
        }
    }

    private fun `Then verify view interaction for success get local search recommendation`(localSearchRecommendationModel: SearchProductModel) {
        verifyOrder {
            productListView.removeLoading()
            productListView.addLocalSearchRecommendation(capture(visitableListSlot))
            productListView.addLoading()
            productListView.updateScrollListener()
        }

        `Then verify visitable list for set empty recommendation page 1`(localSearchRecommendationModel)
    }

    private fun `Then verify visitable list for set empty recommendation page 1`(localSearchRecommendationModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured

        visitableList.first().shouldBeInstanceOf<SearchProductTitleDataView>()
        visitableList.first().assertSearchProductTitle()

        localSearchRecommendationModel.searchProduct.data.productList.forEachIndexed { index, product ->
            visitableList[index + 1].assertOrganicProduct(product, index + 1)
        }
    }

    private fun Visitable<*>.assertSearchProductTitle() {
        val searchProductTitleViewModel = this as SearchProductTitleDataView
        searchProductTitleViewModel.title shouldBe searchProductPageTitle
        searchProductTitleViewModel.isRecommendationTitle shouldBe true
    }

    private fun `Then verify has next page`(expectedHasNextPage: Boolean) {
        productListPresenter.hasNextPage() shouldBe expectedHasNextPage
    }

    @Test
    fun `Get empty local search recommendation failed during local search - page 1`() {
        val searchProductModel = searchProductLocalSearchEmptyJSON.jsonToObject<SearchProductModel>()
        val exception = TestException()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given get local search recommendation will fail`(exception)

        `When Load Data`(searchParameter)

        `Then verify request params for local search recommendation`(listOf(0))
        `Then verify view interaction for no recommendation`(exception)
    }

    private fun `Given get local search recommendation will fail`(exception: Exception) {
        every {
            getLocalSearchRecommendationUseCase.execute(capture(localSearchRecomRequestParamsSlot), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().error(exception)
        }
    }

    private fun `Then verify view interaction for no recommendation`(exception: TestException) {
        exception.isStackTracePrinted shouldBe true

        verify {
            productListView.removeLoading()
        }

        verify(exactly = 0) {
            productListView.addLocalSearchRecommendation(any())
        }
    }

    @Test
    fun `Get empty local search recommendation success during local search - page 2`() {
        val searchProductModel = searchProductLocalSearchEmptyJSON.jsonToObject<SearchProductModel>()
        val localSearchRecommendationModel = emptyLocalSearchRecommendationPage1JSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given get local search recommendation will return SearchProductModel`(localSearchRecommendationModel)
        `Given mechanism to set product position`()
        `Given view already load data`()

        `When view load more data`()

        `Then verify request params for local search recommendation`(listOf(0, 8))
        `Then verify visitable list for set empty recommendation page 2`(localSearchRecommendationModel)
        `Then verify has next page`(false)
    }

    private fun `Given mechanism to set product position`() {
        val lastProductPositionSlot = slot<Int>()

        every { productListView.lastProductItemPositionFromCache }.answers {
            if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
        }

        every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
    }

    private fun `Given view already load data`() {
        productListPresenter.loadData(searchParameter)
    }

    private fun `When view load more data`() {
        productListPresenter.loadMoreData(searchParameter)
    }

    private fun `Then verify visitable list for set empty recommendation page 2`(localSearchRecommendationModel: SearchProductModel) {
        verify {
            productListView.removeLoading()
            productListView.addLocalSearchRecommendation(capture(visitableListSlot))
        }

        val visitableList = visitableListSlot.captured

        visitableList.size shouldBe localSearchRecommendationModel.searchProduct.data.productList.size

        localSearchRecommendationModel.searchProduct.data.productList.forEachIndexed { index, product ->
            visitableList[index].assertOrganicProduct(product, index + 9)
        }
    }
}