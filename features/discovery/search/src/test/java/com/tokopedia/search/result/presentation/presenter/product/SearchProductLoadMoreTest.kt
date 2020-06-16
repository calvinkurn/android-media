package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
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

internal class SearchProductLoadMoreTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()

    @Test
    fun `Load More Data Success`() {
        val searchProductModel = searchProductCommonResponseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModel)
        `Given Product List Presenter already Load Data`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `Then verify load more use case request params`()
        `Then verify view interaction when load more data success`(searchProductModel)
        `Then verify start from is incremented twice`()
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

    private fun `Then verify load more use case request params`() {
        val requestParams = requestParamsSlot.captured

        requestParams.getInt(SearchApiConst.START, -1) shouldBe 8
    }

    private fun `Then verify view interaction when load more data success`(searchProductModel: SearchProductModel) {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView)

            verifyProcessingData(productListView, searchProductModel)

            productListView.showBottomNavigation()
            productListView.updateScrollListener()

            verifyHideLoading(productListView)

            verifyProcessingNextPage(productListView)
        }

        confirmVerified(productListView)
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

        `Then verify view interaction for load data failed with exception`(slotSearchParameterErrorLog, testException, searchProductModelCommon)
        `Then verify logged error message is from search parameter`(slotSearchParameterErrorLog, loadMoreSearchParameter)
    }

    private fun `Given Search Product Load More API will throw exception`(exception: Exception?) {
        every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().error(exception)
        }
    }

    private fun `Then verify view interaction for load data failed with exception`(
            slotSearchParameterErrorLog: CapturingSlot<String>,
            exception: Exception,
            searchProductModelFirstPage: SearchProductModel
    ) {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView)

            verifyProcessingData(productListView, searchProductModelFirstPage)

            productListView.showBottomNavigation()
            productListView.updateScrollListener()

            verifyHideLoading(productListView)

            verifyShowLoadMoreError(productListView, 8)

            productListView.logWarning(capture(slotSearchParameterErrorLog), exception)
        }

        confirmVerified(productListView)
    }

    private fun `Then verify logged error message is from search parameter`(slotSearchParameterErrorLog: CapturingSlot<String>, searchParameter: Map<String, Any>) {
        val message = slotSearchParameterErrorLog.captured

        message shouldBe UrlParamUtils.generateUrlParamString(searchParameter)
    }
}