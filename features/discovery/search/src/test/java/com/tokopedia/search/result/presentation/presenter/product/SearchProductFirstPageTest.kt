package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.TestException
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.error
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelCommon
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber

internal class SearchProductFirstPageTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()

    @Test
    fun `Load Data Success`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        `Given Search Product API will return SearchProductModel`(searchProductModelCommon)

        `When Load Data`(searchParameter)

        `Then verify use case request params START should be 0`()
        `Then verify view interaction when load data success`()
        `Then verify get dynamic filter use case is executed`()
        `Then verify start from is incremented`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify use case request params START should be 0`() {
        val requestParams = requestParamsSlot.captured

        requestParams.getString(SearchApiConst.START, null) shouldBe "0"
    }

    private fun `Then verify view interaction when load data success`() {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView)

            verifyProcessingData(productListView)

            productListView.showBottomNavigation()
            productListView.updateScrollListener()

            verifyHideLoading(productListView)
        }

        confirmVerified(productListView)
    }

    private fun `Then verify get dynamic filter use case is executed`() {
        verify(exactly = 1) { getDynamicFilterUseCase.execute(any(), any()) }
    }

    private fun `Then verify start from is incremented`() {
        val startFrom = productListPresenter.startFrom

        startFrom shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
    }

    @Test
    fun `Load Data Failed with exception`() {
        val testException = TestException()
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.OFFICIAL] = true
        }
        val slotSearchParameterErrorLog = slot<String>()

        `Given Search Product API will throw exception`(testException)

        `When Load Data`(searchParameter)

        `Then verify view interaction for load data failed with exception`(slotSearchParameterErrorLog, testException)
        `Then verify get dynamic filter use case not executed`()
        `Then verify start from is incremented`()
        `Then verify logged error message is from search parameter`(slotSearchParameterErrorLog, searchParameter)
    }

    private fun `Given Search Product API will throw exception`(exception: Exception?) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().error(exception)
        }
    }

    private fun `Then verify view interaction for load data failed with exception`(slotSearchParameterErrorLog: CapturingSlot<String>, exception: Exception) {
        verifyOrder {
            productListView.isAnyFilterActive

            productListView.stopPreparePagePerformanceMonitoring()
            productListView.startNetworkRequestPerformanceMonitoring()

            verifyShowError(productListView)

            productListView.logWarning(capture(slotSearchParameterErrorLog), exception)
        }

        confirmVerified(productListView)
    }

    private fun `Then verify get dynamic filter use case not executed`() {
        verify(exactly = 0) { getDynamicFilterUseCase.execute(any(), any()) }
    }

    private fun `Then verify logged error message is from search parameter`(slotSearchParameterErrorLog: CapturingSlot<String>, searchParameter: Map<String, Any>) {
        val message = slotSearchParameterErrorLog.captured

        message shouldBe UrlParamUtils.generateUrlParamString(searchParameter)
    }

    @Test
    fun `Load Data Success Is First Time Load`() {
        `Given Search Product API will return SearchProductModel`(searchProductModelCommon)
        `Given View is first active tab`()
        `Given View reload data immediately calls load data`()

        `When View is created`()

        `Then verify use case request params START should be 0`()
        `Then verify view interaction when created`()
        `Then verify get dynamic filter use case is executed`()
        `Then verify start from is incremented`()
    }

    private fun `Given View is first active tab`() {
        every { productListView.isFirstActiveTab }.returns(true)
    }

    private fun `Given View reload data immediately calls load data`() {
        every { productListView.reloadData() }.answers {
            val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                it[SearchApiConst.Q] = "samsung"
                it[SearchApiConst.START] = "0"
                it[SearchApiConst.UNIQUE_ID] = "unique_id"
                it[SearchApiConst.USER_ID] = productListPresenter.userId
            }

            productListPresenter.loadData(searchParameter)
        }
    }

    private fun `When View is created`() {
        productListPresenter.onViewCreated()
    }

    private fun `Then verify view interaction when created`() {
        verifyOrder {
            productListView.isFirstActiveTab
            productListView.reloadData()

            productListView.isAnyFilterActive

            verifyShowLoading(productListView)

            verifyProcessingData(productListView)

            productListView.showBottomNavigation()
            productListView.updateScrollListener()

            verifySendTrackingOnFirstTimeLoad(productListView)

            verifyHideLoading(productListView)
        }

        confirmVerified(productListView)
    }
}