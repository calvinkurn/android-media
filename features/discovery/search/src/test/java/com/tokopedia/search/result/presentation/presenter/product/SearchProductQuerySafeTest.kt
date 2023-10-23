package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val searchProductModelWithSafeQueryTrue = "searchproduct/querysafe/query-safe-true.json"
private const val searchProductModelWithSafeQueryFalse = "searchproduct/querysafe/query-safe-false.json"
private const val searchProductModelWithSafeQueryFalseReimagine = "searchproduct/querysafe/query-safe-false-reimagine.json"

internal class SearchProductQuerySafeTest: ProductListPresenterTestFixtures() {
    private val requestParamsSlot = slot<RequestParams>()

    @Test
    fun `Search Product Has Safe Query True`() {
        val searchProductModel = searchProductModelWithSafeQueryTrue.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then Verify Adult PopUp Wont Show`()
    }

    @Test
    fun `Search Product Has Safe Query False`() {
        val searchProductModel = searchProductModelWithSafeQueryFalse.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then Verify Adult PopUp Will Show`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then Verify Adult PopUp Wont Show`() {
        verify(exactly = 0) { productListView.showAdultRestriction() }
    }

    private fun `Then Verify Adult PopUp Will Show`() {
        verify(exactly = 1) { productListView.showAdultRestriction() }
    }

    @Test
    fun `Search Product Has Safe Query False for reimagine`() {
        val searchProductModel = searchProductModelWithSafeQueryFalseReimagine.jsonToObject<SearchProductModel>()
        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then Verify Adult PopUp Will Show`()
    }
}
