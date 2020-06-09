package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val topAdsResponseCodeFirstPage = "searchproduct/topads/response-code-top-ads-first-page.json"
private const val topAdsResponseCodeLoadMore = "searchproduct/topads/response-code-top-ads-load-more.json"

internal class SearchProductTopAdsPositionTest: ProductListPresenterTestFixtures() {

    private val capturedVisitableListFirstPage = slot<List<Visitable<*>>>()
    private val capturedVisitableListLoadMore = slot<List<Visitable<*>>>()
    private var topAdsCount = 1

    @Test
    fun `Test TopAds position in first page is correct`() {
        val searchProductModel = topAdsResponseCodeFirstPage.jsonToObject<SearchProductModel>()
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`(searchParameter)

        `Then verify setProductList is called`()
        `Then verify position is correct`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify setProductList is called`() {
        verify {
            productListView.setProductList(capture(capturedVisitableListFirstPage));
        }
    }

    private fun `Then verify position is correct`() {
        val visitableList = capturedVisitableListFirstPage.captured

        visitableList.forEach {
            if (it is ProductItemViewModel && it.isTopAds) {
                assert(topAdsCount == it.position)
                topAdsCount++
            }
        }
    }

    @Test
    fun `Test TopAds position in load more is correct`() {
        val searchProductModelFirstPage = topAdsResponseCodeFirstPage.jsonToObject<SearchProductModel>()
        val searchProductModelLoadMore = topAdsResponseCodeLoadMore.jsonToObject<SearchProductModel>()
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelLoadMore)
        `Given Product List Presenter already Load Data`(searchParameter)

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `Then verify addProductList is called`()
        `Then verify position is correct for loadMore`()
    }

    private fun `Given Search Product Load More API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given Product List Presenter already Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun createLoadMoreSearchParameter() : Map<String, Any> = mutableMapOf<String, Any>().also {
        it[SearchApiConst.Q] = "samsung"
        it[SearchApiConst.START] = productListPresenter.startFrom
        it[SearchApiConst.UNIQUE_ID] = "unique_id"
        it[SearchApiConst.USER_ID] = productListPresenter.userId
    }

    private fun `When Product List Presenter Load More Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadMoreData(searchParameter)
    }

    private fun `Then verify addProductList is called`() {
        verify {
            productListView.setProductList(capture(capturedVisitableListFirstPage))
            productListView.addProductList(capture(capturedVisitableListLoadMore))
        }
    }

    private fun `Then verify position is correct for loadMore`() {
        val visitableListFirstPage = capturedVisitableListFirstPage.captured
        val visitableListLoadMore = capturedVisitableListLoadMore.captured

        visitableListFirstPage.forEach {
            if (it is ProductItemViewModel && it.isTopAds) {
                topAdsCount++
            }
        }
        visitableListLoadMore.forEach {
            if (it is ProductItemViewModel && it.isTopAds) {
                assert(topAdsCount == it.position)
                topAdsCount++
            }
        }
    }
}