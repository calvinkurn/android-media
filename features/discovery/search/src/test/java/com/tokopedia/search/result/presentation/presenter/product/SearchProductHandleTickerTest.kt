package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelTicker
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber

internal class SearchProductHandleTickerTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()
    private val typeIdSlot = slot<Int>()

    @Test
    fun `Tracker for Ticker Impression is called`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        `Given Search Product API will return SearchProductModel`(searchProductModelTicker)

        `When Load Data`(searchParameter)
        `Then verify track event impression ticker is called`()
        `Then verify type id is the same`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify track event impression ticker is called`() {
        verifyOrder {
            productListView.trackEventImpressionTicker(capture(typeIdSlot))
        }
    }

    private fun `Then verify type id is the same`() {
        val capturedTypeId = typeIdSlot.captured
        val typeId = searchProductModelTicker.searchProduct.ticker.typeId

        assert(capturedTypeId == typeId) {
            "Assertion Failed, actual type_id: ${capturedTypeId}, expected type_id: $typeId"
        }
    }
}