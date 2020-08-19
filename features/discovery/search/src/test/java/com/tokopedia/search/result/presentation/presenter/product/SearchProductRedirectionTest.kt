package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import rx.Subscriber

internal class SearchProductRedirectionTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()
    private val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()
    private val keyword = "produk wardyah"

    @Test
    fun `Load Data Success With Redirection Response Code 6`() {
        val searchProductModel = "searchproduct/redirection/redirection.json".jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with redirection`(searchProductModel)

        `When Load Data`()

        `Then verify use case request params START should be 0`()
        `Then verify view interaction for redirection`(searchProductModel.searchProduct.data.redirection.redirectApplink)
        `Then verify start from is incremented`()
    }

    private fun `Given Search Product API will return SearchProductModel with redirection`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = keyword
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify use case request params START should be 0`() {
        val requestParams = requestParamsSlot.captured

        requestParams.getString(SearchApiConst.START, null) shouldBe "0"
    }

    private fun `Then verify view interaction for redirection`(redirectApplink: String) {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView)

            productListView.redirectSearchToAnotherPage(redirectApplink)

            verifyHideLoading(productListView)
        }
    }

    private fun `Then verify start from is incremented`() {
        val startFrom = productListPresenter.startFrom

        startFrom shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
    }

    @Test
    fun `Load Data Success With Redirection Response Code 9`() {
        val searchProductModel = "searchproduct/redirection/redirection-response-code-9.json".jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with redirection`(searchProductModel)

        `Given View getQueryKey will return the keyword`()
        `When Load Data`()

        `Then verify use case request params START should be 0`()
        `Then verify view interaction for redirection with response code 9`(searchProductModel.searchProduct.data.redirection.redirectApplink)

        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "65",
                categoryNameMapping = "Handphone & Tablet",
                relatedKeyword = "none - none"
        )
        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    private fun `Given View getQueryKey will return the keyword`() {
        every { productListView.queryKey } returns keyword
    }

    private fun `Then verify view interaction for redirection with response code 9`(redirectApplink: String) {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView)

            productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
            productListView.redirectSearchToAnotherPage(redirectApplink)

            verifyHideLoading(productListView)
        }
    }

    private fun `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel: GeneralSearchTrackingModel) {
        val actualGeneralSearchTrackingModel = generalSearchTrackingModelSlot.captured

        actualGeneralSearchTrackingModel shouldBe expectedGeneralSearchTrackingModel
    }
}