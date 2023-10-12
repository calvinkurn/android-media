package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.domain.model.ProductTopAdsModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.ads.AdsLowOrganic
import com.tokopedia.search.result.product.ads.AdsLowOrganicTitleDataView
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateKeywordDataView
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.stubExecute
import com.tokopedia.search.stubExecuteFail
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test

private const val emptyState = "searchproduct/adsloworganic/empty-state.json"
private const val emptyStateResponseCode1 = "searchproduct/adsloworganic/empty-state-response-code-1.json"
private const val broadMatchEmptyState = "searchproduct/adsloworganic/broadmatch-empty-state.json"
private const val topAdsResponse = "searchproduct/topads-response.json"
private const val searchProductWithTopAdsPage1 = "searchproduct/adsloworganic/page1-with-topads.json"
private const val emptyTopAdsResponse = "searchproduct/adsloworganic/empty-topads-response.json"

internal class SearchProductAdsInLowOrganicTest: ProductListPresenterTestFixtures() {

    private val keyword = "bekal kanguru"
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `response code 0 should show ads below no result view`() {
        val searchProductModel = emptyState.jsonToObject<SearchProductModel>()

        `Given search product first page is successful`(searchProductModel)
        `Given query key will return keyword`()
        `Given visitable list is captured`()

        `When load data`()

        `Then verify empty state data view`()
        `Then verify ads in low organic supply title`(keyword)
        `Then verify product item data view ads`(
            searchProductModel,
            searchProductModel.topAdsModel,
            1
        )
        `Then verify recommendation is not called`()
    }
    @Test
    fun `response code 1 should show ads below no result view`() {
        val searchProductModel = emptyStateResponseCode1.jsonToObject<SearchProductModel>()

        `Given search product first page is successful`(searchProductModel)
        `Given query key will return keyword`()
        `Given visitable list is captured`()

        `When load data`()

        `Then verify empty state data view`()
        `Then verify ads in low organic supply title`(keyword)
        `Then verify product item data view ads`(
            searchProductModel,
            searchProductModel.topAdsModel,
            1
        )
        `Then verify recommendation is not called`()
    }

    private fun `Given ads in low organic experiment enabled`() {
        every {
            abTestRemoteConfig.getString(AdsLowOrganic.EXP_NAME, "")
        } returns AdsLowOrganic.VAR_ADS
    }

    private fun `Given search product first page is successful`(searchProductModel: SearchProductModel) {
        searchProductFirstPageUseCase.stubExecute(searchProductModel)
    }

    private fun `Given query key will return keyword`() {
        every { productListView.queryKey } returns keyword
    }

    private fun `Given visitable list is captured`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs
        every { productListView.addProductList(capture(visitableListSlot)) } just runs
        every { viewUpdater.setItems(capture(visitableListSlot)) } just runs
        every { viewUpdater.appendItems(capture(visitableListSlot)) } just runs
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then verify empty state data view`() {
        val emptyStateKeywordDataView = visitableList.findEmptyStateDataView()

        assertTrue(emptyStateKeywordDataView.isShowAdsLowOrganic)
        assertTrue(emptyStateKeywordDataView.verticalSeparator.hasBottomSeparator)
    }

    private fun List<Visitable<*>>.findEmptyStateDataView() =
        find { it is EmptyStateKeywordDataView } as? EmptyStateKeywordDataView
            ?: throw AssertionError("Empty State Keyword Data View not found")

    private fun `Then verify ads in low organic supply title`(keyword: String) {
        val adsLowOrganicSupplyDataView = visitableList.findAdsLowOrganicSupplyTitleDataView()

        assertThat(adsLowOrganicSupplyDataView.keyword, `is`(keyword))
    }

    private fun List<Visitable<*>>.findAdsLowOrganicSupplyTitleDataView() =
        find { it is AdsLowOrganicTitleDataView } as? AdsLowOrganicTitleDataView
            ?: throw AssertionError("Ads Low Organic Title Data View not found")

    private fun `Then verify product item data view ads`(
        searchProductModel: SearchProductModel,
        topAdsModel: TopAdsModel,
        firstTopAdsPosition: Int,
    ) {
        val firstProductAdsIndex = visitableList.indexOfFirst { it is ProductItemDataView }
        val productAdsList = visitableList.subList(firstProductAdsIndex, visitableList.size)

        topAdsModel.data.forEachIndexed { index, data ->
            val expectedPosition = firstTopAdsPosition + index
            productAdsList[index].assertTopAdsProduct(
                data,
                expectedPosition,
                searchProductModel.getProductListType(),
                searchProductModel.isShowButtonAtc,
            )
        }
    }

    private fun `Then verify recommendation is not called`() {
        verify(exactly = 0) { recommendationUseCase.execute(any(), any()) }
    }

    @Test
    fun `response code 0 should show ads below no result view for next page`() {
        val searchProductModel = emptyState.jsonToObject<SearchProductModel>()
        val topAdsModelNextPage = topAdsResponse.jsonToObject<ProductTopAdsModel>().topAdsModel
        val requestParamsSlot = slot<RequestParams>()
        val requestParams by lazy { requestParamsSlot.captured }

        `Given search product first page is successful`(searchProductModel)
        `Given search product top ads next page is successful `(topAdsModelNextPage, requestParamsSlot)
        `Given query key will return keyword`()
        `Given view already load data`()
        `Given visitable list is captured`()

        `When load more data`()

        `Then verify product item data view ads`(
            searchProductModel,
            topAdsModelNextPage,
            searchProductModel.topAdsModel.data.size + 1
        )
        `Then verify top ads request params`(requestParams, 2)
    }

    private fun `Given search product top ads next page is successful `(
        topAdsModel: TopAdsModel,
        requestParamsSlot: CapturingSlot<RequestParams> = slot(),
    ) {
        searchProductTopAdsUseCase.stubExecute(topAdsModel, requestParamsSlot)
    }

    private fun `Given view already load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `When load more data`() {
        productListPresenter.loadMoreData(mapOf())
    }

    private fun `Then verify top ads request params`(
        requestParams: RequestParams,
        expectedPage: Int,
    ) {
        assertThat(requestParams.parameters[TopAdsParams.KEY_PAGE], `is`(expectedPage))
    }

    @Test
    fun `response code 0 should show ads for third page`() {
        val searchProductModel = emptyState.jsonToObject<SearchProductModel>()
        val topAdsModelNextPage = topAdsResponse.jsonToObject<ProductTopAdsModel>().topAdsModel
        val requestParamsSlot = slot<RequestParams>()
        val requestParams by lazy { requestParamsSlot.captured }

        `Given search product first page is successful`(searchProductModel)
        `Given search product top ads next page is successful `(topAdsModelNextPage, requestParamsSlot)
        `Given view already load data`()
        `Given view already load more data`()

        `When load more data`()

        `Then verify top ads request params`(requestParams, 3)
    }

    private fun `Given view already load more data`() {
        productListPresenter.loadMoreData(mapOf())
    }

    @Test
    fun `enable experiment to show ads with broad match`() {
        val searchProductModel = broadMatchEmptyState.jsonToObject<SearchProductModel>()

        `Given ads in low organic experiment enabled`()
        `Given search product first page is successful`(searchProductModel)
        `Given query key will return keyword`()
        `Given visitable list is captured`()

        `When load data`()

        `Then verify ads in low organic supply title`(keyword)
        `Then verify product item data view ads`(
            searchProductModel,
            searchProductModel.topAdsModel,
            1
        )
        `Then verify broad match is shown`(searchProductModel.topAdsModel.data.size + 1)
        `Then verify recommendation is not called`()
    }

    private fun `Then verify broad match is shown`(suggestionDataViewIndex: Int) {
        assertThat(
            visitableList[suggestionDataViewIndex],
            instanceOf(SuggestionDataView::class.java)
        )

        assertTrue(
            visitableList
                .subList(suggestionDataViewIndex + 1, visitableList.size)
                .all { it is BroadMatchDataView }
        )
    }

    @Test
    fun `enable experiment to show ads with broad match for next page`() {
        val searchProductModel = broadMatchEmptyState.jsonToObject<SearchProductModel>()
        val topAdsModelNextPage = topAdsResponse.jsonToObject<ProductTopAdsModel>().topAdsModel
        val requestParamsSlot = slot<RequestParams>()
        val requestParams by lazy { requestParamsSlot.captured }

        `Given ads in low organic experiment enabled`()
        `Given search product first page is successful`(searchProductModel)
        `Given search product top ads next page is successful `(topAdsModelNextPage, requestParamsSlot)
        `Given query key will return keyword`()
        `Given visitable list is captured`()
        `Given view already load data`()

        `When load more data`()

        `Then verify product item data view ads`(
            searchProductModel,
            topAdsModelNextPage,
            searchProductModel.topAdsModel.data.size + 1
        )
        `Then verify top ads request params`(requestParams, 2)
    }

    @Test
    fun `response code 0 will load more until ads response is empty`() {
        val searchProductModel = searchProductWithTopAdsPage1.jsonToObject<SearchProductModel>()
        val topAdsModelNextPage = emptyTopAdsResponse.jsonToObject<ProductTopAdsModel>().topAdsModel

        `Given search product first page is successful`(searchProductModel)
        `Given search product top ads next page is successful `(topAdsModelNextPage)
        `Given query key will return keyword`()
        `Given view already load data`()
        `Given view already load more data`()
        `Given visitable list is captured`()

        `When load more data`()

        `Then verify top ads use case only called once`()
    }

    private fun `Then verify top ads use case only called once`() {
        verify(exactly = 1) { searchProductTopAdsUseCase.execute(any(), any()) }
    }

    @Test
    fun `response code 0 will load more until ads response is error`() {
        val searchProductModel = searchProductWithTopAdsPage1.jsonToObject<SearchProductModel>()

        `Given search product first page is successful`(searchProductModel)
        `Given top ads use case will error`()
        `Given query key will return keyword`()
        `Given view already load data`()
        `Given view already load more data`()
        `Given visitable list is captured`()

        `When load more data`()

        `Then verify top ads use case only called once`()
    }

    private fun `Given top ads use case will error`() {
        searchProductTopAdsUseCase.stubExecuteFail(Exception())
    }
}
