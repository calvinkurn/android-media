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
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import rx.Subscriber

internal class SearchProductFirstPageTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Load Data Success`() {
        val searchProductModel = searchProductFirstPageJSON.jsonToObject<SearchProductModel>()
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View getQueryKey will return the keyword`(searchParameter[SearchApiConst.Q].toString())

        `When Load Data`(searchParameter)

        `Then verify use case request params`()
        `Then verify view interaction when load data success`(
            searchProductModel.searchProduct.data.autocompleteApplink,
        )
        `Then verify start from is incremented`()
        `Then verify visitable list with product items`(
            visitableListSlot = visitableListSlot,
            searchProductModel = searchProductModel,
        )
        `Then assert page component id`(searchProductModel.searchProduct.header.componentId)
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given View getQueryKey will return the keyword`(keyword: String) {
        every { productListView.queryKey } returns keyword
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify use case request params`() {
        val requestParams = requestParamsSlot.captured

        val params = requestParams.getSearchProductParams()
        params.getOrDefault(SearchApiConst.START, null) shouldBe "0"

        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_PRODUCT_ADS, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_HEADLINE_ADS, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_GLOBAL_NAV, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET, false) shouldBe false
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_TDN_BANNER, false) shouldBe false
    }

    private fun `Then verify view interaction when load data success`(
        expectedAutoCompleteApplink: String,
    ) {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView, performanceMonitoring)

            verifyProcessingData(
                productListView,
                performanceMonitoring,
                visitableListSlot,
                expectedAutoCompleteApplink,
            )

            productListView.updateScrollListener()

            verifySendTrackingOnFirstTimeLoad(productListView)

            verifyHideLoading(productListView)
        }
    }

    private fun `Then verify start from is incremented`() {
        val startFrom = productListPresenter.startFrom

        startFrom shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
    }

    private fun `Then assert page component id`(expectedComponentId: String) {
        assertThat(productListPresenter.pageComponentId, `is`(expectedComponentId))
    }

    @Test
    fun `Load Data Success for reimagine`() {
        val searchProductModel = searchProductFirstPageReimagineJSON.jsonToObject<SearchProductModel>()
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View getQueryKey will return the keyword`(searchParameter[SearchApiConst.Q].toString())

        `When Load Data`(searchParameter)

        `Then verify use case request params`()
        `Then verify view interaction when load data success`(
            searchProductModel.searchProductV5.header.autocompleteApplink
        )
        `Then verify start from is incremented`()
        `Then verify visitable list with product items for reimagine`(
            visitableListSlot = visitableListSlot,
            searchProductModel = searchProductModel,
        )
        `Then assert page component id`(searchProductModel.searchProductV5.header.componentID)
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
        `Then verify start from is not incremented`()
        `Then verify logged error message is from search parameter`(slotSearchParameterErrorLog, requestParamsSlot.captured.getSearchProductParams())
    }

    private fun `Given Search Product API will throw exception`(exception: Exception?) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().error(exception)
        }
    }

    private fun `Then verify view interaction for load data failed with exception`(slotSearchParameterErrorLog: CapturingSlot<String>, exception: Exception) {
        verifyOrder {
            productListView.isAnyFilterActive
            productListView.isAnySortActive

            performanceMonitoring.stopPreparePagePerformanceMonitoring()
            performanceMonitoring.startNetworkRequestPerformanceMonitoring()

            verifyShowError(productListView)

            productListView.logWarning(capture(slotSearchParameterErrorLog), exception)

            productListView.sendTrackingByteIO(false)
        }

        confirmVerified(productListView)
    }

    private fun `Then verify start from is not incremented`() {
        val startFrom = productListPresenter.startFrom

        startFrom shouldBe 0
    }

    private fun `Then verify logged error message is from search parameter`(slotSearchParameterErrorLog: CapturingSlot<String>, searchParameter: Map<String, Any>) {
        val message = slotSearchParameterErrorLog.captured

        @Suppress("UNCHECKED_CAST")
        message shouldBe UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any?>)
    }

    @Test
    fun `Load Data Success Is First Time Load`() {
        val searchProductModel = searchProductFirstPageJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given View reload data immediately calls load data`()
        `Given View getQueryKey will return the keyword`("samsung")

        `When View is created`()

        `Then verify use case request params`()
        `Then verify view interaction when created`(
            searchProductModel.searchProduct.data.autocompleteApplink
        )
        `Then verify start from is incremented`()
        `Then verify visitable list with product items`(
            visitableListSlot = visitableListSlot,
            searchProductModel = searchProductModel,
        )
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

    private fun `Then verify view interaction when created`(
        expectedAutoCompleteApplink: String
    ) {
        verifyOrder {
            productListView.isFirstActiveTab
            productListView.reloadData()

            productListView.isAnyFilterActive

            verifyShowLoading(productListView, performanceMonitoring)

            verifyProcessingData(
                productListView,
                performanceMonitoring,
                visitableListSlot,
                expectedAutoCompleteApplink
            )

            productListView.updateScrollListener()

            verifySendTrackingOnFirstTimeLoad(productListView)

            verifyHideLoading(productListView)
        }
    }
}
