package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.START
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateDataView
import com.tokopedia.search.result.product.fulfillment.FulfillmentFilter.Companion.LOAD_EMPTY_PRODUCT_THRESHOLD
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import rx.Subscriber

private const val searchProductNotEmptyJSON = "searchproduct/with-topads.json"
private const val emptyTotalDataZeroJSON = "searchproduct/fulfillmentfilter/empty-total-data-0.json"
private const val emptyTotalDataNotZeroJSON = "searchproduct/fulfillmentfilter/empty-total-data-not-0.json"

internal class SearchProductFulfillmentFilter: ProductListPresenterTestFixtures() {

    private val emptyProductTotalDataNotZero =
        emptyTotalDataNotZeroJSON.jsonToObject<SearchProductModel>()
    private val emptyProductTotalDataZero =
        emptyTotalDataZeroJSON.jsonToObject<SearchProductModel>()
    private val notEmptyProduct =
        searchProductNotEmptyJSON.jsonToObject<SearchProductModel>()

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }
    private val searchParameter: Map<String, Any> = mapOf(
        SearchApiConst.Q to "samsung",
        SearchApiConst.IS_FULFILLMENT to "true",
    )

    @Test
    fun `all top ads products shown before organic products for fulfillment filter`() {
        `Given search product first page API will be successful`(notEmptyProduct)
        `Given visitable list will be captured`()

        `When load data`(searchParameter)

        `Then assert all top ads products shown before all organics products`(notEmptyProduct)
    }

    private fun `Given search product first page API will be successful`(searchProductModel: SearchProductModel) {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given visitable list will be captured`() {
        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs
    }

    private fun `When load data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then assert all top ads products shown before all organics products`(
        searchProductModel: SearchProductModel
    ) {
        val productItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()
        val topAdsCount = searchProductModel.topAdsModel.data.size
        val topAdsProductItemDataViewList = productItemDataViewList.subList(0, topAdsCount)

        assertThat(topAdsProductItemDataViewList.all { it.isTopAds }, `is`(true))
    }

    @Test
    fun `show empty state if total_data is 0 with fulfillment filter`() {
        `Given search product first page API will be successful`(emptyProductTotalDataZero)
        `Given visitable list will be captured`()

        `When load data`(searchParameter)

        `Then verify visitable list contains empty state`()
    }

    private fun `Then verify visitable list contains empty state`() {
        assertThat(visitableList.any { it is EmptyStateDataView }, `is`(true))
    }

    @Test
    fun `load next page when first page empty with fulfillment filter by incrementing START params`() {
        `Given search product first page API responses with different START params`()

        `When load data`(searchParameter)

        `Then verify search product API is called again with second page params`()
    }

    private fun `Given search product first page API responses with different START params`() {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            val parameter = firstArg<RequestParams>().getSearchProductParams()
            val searchProductModel =
                if (parameter[START].toString() == "0")
                    emptyProductTotalDataNotZero
                else
                    notEmptyProduct

            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Then verify search product API is called again with second page params`() {
        val requestParamsSlot = mutableListOf<RequestParams>()
        val requestParams by lazy { requestParamsSlot.last() }

        verify(exactly = 2) {
            searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any())
        }

        val searchProductParams = requestParams.getSearchProductParams()
        val actualStartParam = searchProductParams[START].toString().toIntOrZero()
        val expectedStartParam = 8

        assertThat(actualStartParam, `is`(expectedStartParam))
    }

    @Test
    fun `load next page when second page empty with fulfillment filter by incrementing START params`() {
        `Given search product first page API will be successful`(notEmptyProduct)
        `Given view already load data`(searchParameter)
        `Given search product load more API responses with different START params`()

        `When load more data`(searchParameter)

        `Then verify search product load more API is called again with third page params`()
    }

    private fun `Given search product load more API responses with different START params`() {
        every {
            searchProductLoadMoreUseCase.execute(any(), any())
        } answers {
            val parameter = firstArg<RequestParams>().getSearchProductParams()
            val searchProductModel =
                if (parameter[START].toString() == "8")
                    emptyProductTotalDataNotZero
                else
                    notEmptyProduct

            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view already load data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `When load more data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadMoreData(searchParameter)
    }

    private fun `Then verify search product load more API is called again with third page params`() {
        val requestParamsSlot = mutableListOf<RequestParams>()
        val requestParams by lazy { requestParamsSlot.last() }

        verify(exactly = 2) {
            searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any())
        }

        val searchProductParams = requestParams.getSearchProductParams()
        val actualStartParam = searchProductParams[START].toString().toIntOrZero()
        val expectedStartParam = 16

        assertThat(actualStartParam, `is`(expectedStartParam))
    }

    @Test
    fun `load first page with threshold when first page empty with fulfillment filter`() {
        `Given search product first page API will be successful`(emptyProductTotalDataNotZero)
        `Given visitable list will be captured`()

        `When load data`(searchParameter)

        `Then verify search product first page API only called for certain threshold`()
        `Then verify visitable list contains empty state`()
    }

    private fun `Then verify search product first page API only called for certain threshold`() {
        verify(exactly = LOAD_EMPTY_PRODUCT_THRESHOLD) {
            searchProductFirstPageUseCase.execute(any(), any())
        }
    }

    @Test
    fun `load next page with threshold when second page empty with fulfillment filter`() {
        `Given search product first page API will be successful`(notEmptyProduct)
        `Given view already load data`(searchParameter)
        `Given search product load more API will be successful`(emptyProductTotalDataNotZero)

        `When load more data`(searchParameter)

        `Then verify search product load more API only called for certain threshold`()
    }

    private fun `Given search product load more API will be successful`(
        searchProductModel: SearchProductModel
    ) {
        every {
            searchProductLoadMoreUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Then verify search product load more API only called for certain threshold`() {
        verify(exactly = LOAD_EMPTY_PRODUCT_THRESHOLD) {
            searchProductLoadMoreUseCase.execute(any(), any())
        }
    }

    @Test
    fun `empty products threshold only counts for consecutive empty products response - 1`() {
        `Given search product APIs responses` { parameter ->
            when (parameter[START].toString()) {
                "0" -> emptyProductTotalDataNotZero
                "8" -> notEmptyProduct
                "16" -> emptyProductTotalDataNotZero
                else -> notEmptyProduct
            }
        }

        productListPresenter.loadData(searchParameter)
        productListPresenter.loadMoreData(searchParameter)

        verify(exactly = 2) {
            searchProductFirstPageUseCase.execute(any(), any())
        }

        verify(exactly = 2) {
            searchProductLoadMoreUseCase.execute(any(), any())
        }
    }

    @Test
    fun `empty products threshold only counts for consecutive empty products response - 2`() {
        `Given search product APIs responses` { parameter ->
            when (parameter[START].toString()) {
                "0" -> notEmptyProduct
                "8" -> emptyProductTotalDataNotZero
                "16" -> notEmptyProduct
                "24" -> emptyProductTotalDataNotZero
                else -> notEmptyProduct
            }
        }

        productListPresenter.loadData(searchParameter)
        productListPresenter.loadMoreData(searchParameter)
        productListPresenter.loadMoreData(searchParameter)

        verify(exactly = 1) {
            searchProductFirstPageUseCase.execute(any(), any())
        }

        verify(exactly = 4) {
            searchProductLoadMoreUseCase.execute(any(), any())
        }
    }

    private fun `Given search product APIs responses`(
        getSearchProduct: (Map<String, Any>) -> SearchProductModel,
    ) {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            val searchProductParams = firstArg<RequestParams>().getSearchProductParams()
            val subscriber = secondArg<Subscriber<SearchProductModel>>()

            subscriber.complete(getSearchProduct(searchProductParams))
        }

        every {
            searchProductLoadMoreUseCase.execute(any(), any())
        } answers {
            val searchProductParams = firstArg<RequestParams>().getSearchProductParams()
            val subscriber = secondArg<Subscriber<SearchProductModel>>()

            subscriber.complete(getSearchProduct(searchProductParams))
        }
    }
}