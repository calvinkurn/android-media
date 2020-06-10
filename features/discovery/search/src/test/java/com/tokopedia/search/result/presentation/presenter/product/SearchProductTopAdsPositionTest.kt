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

private const val topAdsResponseFirstPage = "searchproduct/topads/response-top-ads-first-page.json"
private const val topAdsResponseLoadMore = "searchproduct/topads/response-top-ads-load-more.json"

internal class SearchProductTopAdsPositionTest: ProductListPresenterTestFixtures() {

    private val capturedVisitableListFirstPage = slot<List<Visitable<*>>>()
    private val capturedVisitableListLoadMore = slot<List<Visitable<*>>>()

    @Test
    fun `Test TopAds position in first page is correct`() {
        val searchProductModel = topAdsResponseFirstPage.jsonToObject<SearchProductModel>()
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`(searchParameter)

        `Then verify setProductList is called`()

        val visitableList = mutableListOf<Visitable<*>>()
        visitableList.addAll(capturedVisitableListFirstPage.captured)
        `Then verify position is correct`(visitableList)
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

    private fun `Then verify position is correct`(list: MutableList<Visitable<*>>) {
        var topAdsCount = 1

        list.forEach {
            if (it is ProductItemViewModel && it.isTopAds) {
                assert(topAdsCount == it.position) {
                    "Assertion failed, TopAds Position : ${it.position}, Actual Position : $topAdsCount"
                }
                topAdsCount++
            }
        }
    }

    @Test
    fun `Test TopAds position in load more is correct`() {
        val searchProductModelFirstPage = topAdsResponseFirstPage.jsonToObject<SearchProductModel>()
        val searchProductModelLoadMore = topAdsResponseLoadMore.jsonToObject<SearchProductModel>()
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


        val visitableList = mutableListOf<Visitable<*>>()
        visitableList.addAll(capturedVisitableListFirstPage.captured)
        visitableList.addAll(capturedVisitableListLoadMore.captured)
        `Then verify position is correct`(visitableList)
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
}