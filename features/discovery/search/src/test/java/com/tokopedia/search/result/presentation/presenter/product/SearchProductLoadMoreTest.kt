package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.*
import com.tokopedia.search.TestException
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.error
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val searchProductFirstPage8ProductsJSON = "searchproduct/loaddata/first-page-8-products.json"
private const val searchProductThirdPageJSON = "searchproduct/loaddata/third-page.json"
private const val searchProductThirdPageReimagineJSON = "searchproduct/loaddata/reimagine/third-page.json"

internal class SearchProductLoadMoreTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Load More Data Success`() {
        val searchProductModelFirstPage = searchProductFirstPageJSON.jsonToObject<SearchProductModel>()
        val searchProductModelSecondPage = searchProductSecondPageJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelSecondPage)
        `Given Mechanism to save and get product position from cache`()
        `Given Product List Presenter already Load Data`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        val expectedStart = 8
        `Then verify load more use case request params`(expectedStart, searchProductModelFirstPage.searchProduct.header.additionalParams)
        `Then verify view interaction when load more data success`(
            searchProductModelFirstPage.searchProduct.data.autocompleteApplink,
        )
        `Then verify start from is incremented twice`()
        val topAdsIndexStart = searchProductModelFirstPage.topAdsModel.data.size
        val organicIndexStart = searchProductModelFirstPage.searchProduct.data.productList.size
        `Then verify visitable list with product items`(
            visitableListSlot = visitableListSlot,
            searchProductModel = searchProductModelSecondPage,
            topAdsPositionStart = topAdsIndexStart,
            organicPositionStart = organicIndexStart,
        )
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given Search Product Load More API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given Mechanism to save and get product position from cache`() {
        val lastProductPositionSlot = slot<Int>()

        every { productListView.lastProductItemPositionFromCache }.answers {
            if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
        }

        every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
    }

    private fun `Given Product List Presenter already Load Data`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

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

    private fun `Then verify load more use case request params`(expectedStart: Int, additionalParams: String) {
        val requestParams = requestParamsSlot.captured

        val params = requestParams.getSearchProductParams()
        params[SearchApiConst.START] shouldBe expectedStart

        verifyRequestContainsAdditionalParams(params, additionalParams)

        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_PRODUCT_ADS, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_HEADLINE_ADS, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_GLOBAL_NAV, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET, false) shouldBe false
    }

    private fun verifyRequestContainsAdditionalParams(params: Map<String, Any>, additionalParams: String) {
        UrlParamUtils.getParamMap(additionalParams).forEach { (key, value) ->
            params[key] shouldBe value
        }
    }

    private fun `Then verify view interaction when load more data success`(
        expectedAutoCompleteApplink: String,
    ) {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView, performanceMonitoring)

            verifyProcessingData(
                productListView,
                performanceMonitoring,
                slot(),
                expectedAutoCompleteApplink,
            )

            productListView.updateScrollListener()

            verifyHideLoading(productListView)

            verifyProcessingNextPage(productListView, visitableListSlot)
        }
    }

    private fun `Then verify start from is incremented twice`() {
        val startFrom = productListPresenter.startFrom

        startFrom shouldBe (SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt() * 2)
    }

    @Test
    fun `Load More Data Failed with exception`() {
        val testException = TestException()
        val slotSearchParameterErrorLog = slot<String>()
        val searchProductModelCommon = searchProductCommonResponseJSON.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModelCommon)
        `Given Search Product Load More API will throw exception`(testException)
        `Given Product List Presenter already Load Data`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `Then verify start from is not incremented`()
        `Then verify view interaction for load data failed with exception`(slotSearchParameterErrorLog, testException, searchProductModelCommon)
        `Then verify logged error message is from search parameter`(slotSearchParameterErrorLog, requestParamsSlot.captured.getSearchProductParams())
    }

    private fun `Given Search Product Load More API will throw exception`(exception: Exception?) {
        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().error(exception)
        }
    }

    private fun `Then verify start from is not incremented`() {
        val startFrom = productListPresenter.startFrom

        startFrom shouldBe 8
    }

    private fun `Then verify view interaction for load data failed with exception`(
            slotSearchParameterErrorLog: CapturingSlot<String>,
            exception: Exception,
            searchProductModelFirstPage: SearchProductModel
    ) {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView, performanceMonitoring)

            verifyProcessingData(
                productListView,
                performanceMonitoring,
                slot(),
                searchProductModelFirstPage.searchProduct.data.autocompleteApplink,
            )

            productListView.updateScrollListener()

            verifyHideLoading(productListView)

            verifyShowLoadMoreError(productListView)

            productListView.logWarning(capture(slotSearchParameterErrorLog), exception)
        }
    }

    private fun `Then verify logged error message is from search parameter`(slotSearchParameterErrorLog: CapturingSlot<String>, searchParameter: Map<String, Any>) {
        val message = slotSearchParameterErrorLog.captured

        @Suppress("UNCHECKED_CAST")
        message shouldBe UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any?>)
    }

    @Test
    fun `Load More for third page should send additional params from second page`() {
        val searchProductModelFirstPage = searchProductFirstPageJSON.jsonToObject<SearchProductModel>()
        val searchProductModelSecondPage = searchProductSecondPageJSON.jsonToObject<SearchProductModel>()
        val searchProductModelThirdPage = searchProductThirdPageJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)

        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelSecondPage)
        } andThenAnswer {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelThirdPage)
        }

        `Given Mechanism to save and get product position from cache`()
        `Given Product List Presenter already Load Data`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `When Product List Presenter Load More Data`(createLoadMoreSearchParameter())

        val expectedStart = 16
        `Then verify load more use case request params`(expectedStart, searchProductModelSecondPage.searchProduct.header.additionalParams)
    }

    @Test
    fun `do not load more if all product is fetched`() {
        val testException = TestException()
        val searchProductModelFirstPage = searchProductFirstPage8ProductsJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given Search Product Load More API will throw exception`(testException)
        `Given Product List Presenter already Load Data`()

        `When Product List Presenter Load More Data`(mapOf())

        verify (exactly = 0) {
            searchProductLoadMoreUseCase.execute(any(), any())
        }
    }

    @Test
    fun `Load More Data Success for reimagine`() {
        val searchProductModelFirstPage = searchProductFirstPageReimagineJSON.jsonToObject<SearchProductModel>()
        val searchProductModelSecondPage = searchProductSecondPageReimagineJSON.jsonToObject<SearchProductModel>()
        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelSecondPage)
        `Given Mechanism to save and get product position from cache`()
        `Given Product List Presenter already Load Data`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        val expectedStart = 8
        `Then verify load more use case request params`(
            expectedStart,
            searchProductModelFirstPage.searchProductV5.header.additionalParams
        )
        `Then verify view interaction when load more data success`(
            searchProductModelFirstPage.searchProductV5.header.autocompleteApplink
        )
        `Then verify start from is incremented twice`()

        val topAdsIndexStart = searchProductModelFirstPage.topAdsModel.data.size
        val organicIndexStart = searchProductModelFirstPage.searchProductV5.data.productList.size
        `Then verify visitable list with product items for reimagine`(
            visitableListSlot = visitableListSlot,
            searchProductModel = searchProductModelSecondPage,
            topAdsPositionStart = topAdsIndexStart,
            organicPositionStart = organicIndexStart,
        )
    }

    @Test
    fun `Load More for third page should send additional params from second page for reimagine`() {
        val searchProductModelFirstPage = searchProductFirstPageReimagineJSON.jsonToObject<SearchProductModel>()
        val searchProductModelSecondPage = searchProductSecondPageReimagineJSON.jsonToObject<SearchProductModel>()
        val searchProductModelThirdPage = searchProductThirdPageReimagineJSON.jsonToObject<SearchProductModel>()
        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)

        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelSecondPage)
        } andThenAnswer {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelThirdPage)
        }

        `Given Mechanism to save and get product position from cache`()
        `Given Product List Presenter already Load Data`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `When Product List Presenter Load More Data`(createLoadMoreSearchParameter())

        val expectedStart = 16
        `Then verify load more use case request params`(expectedStart, searchProductModelSecondPage.searchProductV5.header.additionalParams)
    }
}
