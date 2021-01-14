package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verifyOrder
import org.junit.Test
import rx.Subscriber

internal class SearchProductRedirectionTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()
    private val keyword = "produk wardyah"

    @Test
    fun `Load Data Success With Redirection`() {
        val searchProductModel = "searchproduct/redirection/redirection.json".jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel with redirection`(searchProductModel)
        `Given View getQueryKey will return the keyword`()

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

    private fun `Given View getQueryKey will return the keyword`() {
        every { productListView.queryKey } returns keyword
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

        requestParams.getSearchProductParams()[SearchApiConst.START] shouldBe "0"
    }

    private fun `Then verify view interaction for redirection`(redirectApplink: String) {
        verifyOrder {
            productListView.isAnyFilterActive

            verifyShowLoading(productListView)

            productListView.sendTrackingGTMEventSearchAttempt(any())
            productListView.redirectSearchToAnotherPage(redirectApplink)

            verifyHideLoading(productListView)
        }
    }

    private fun `Then verify start from is incremented`() {
        val startFrom = productListPresenter.startFrom

        startFrom shouldBe SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
    }
}