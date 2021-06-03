package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.TestException
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.error
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class SearchProductGetProductCountTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()

    @Test
    fun `Get product count with null parameter should call set product count with empty string`() {
        `When get product count`(null)

        `Then assert view set product count`("0")
    }

    private fun `When get product count`(mapParameter: Map<String, String>?) {
        productListPresenter.getProductCount(mapParameter)
    }

    private fun `Then assert view set product count`(productCountText: String) {
        verify {
            productListView.setProductCount(productCountText)
        }
    }

    @Test
    fun `Get product count with parameter should call get product count use case with parameters and size = 0`() {
        val successfulProductCountText = "10rb Produk"
        `Given Get Product Count Use Case will be successful`(successfulProductCountText)

        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true.toString(), SearchApiConst.USER_WAREHOUSE_ID to warehouseId)
        `When get product count`(mapParameter)

        `Then assert request params contains map parameters`(mapParameter)
        `Then assert request params has key ROWS with value 0`()
        `Then assert view set product count`(successfulProductCountText)
    }

    private fun `Given Get Product Count Use Case will be successful`(productCountText: String) {
        every { getProductCountUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<String>>().complete(productCountText)
        }
    }

    private fun `Then assert request params contains map parameters`(mapParameter: Map<String, String>) {
        val requestParams = requestParamsSlot.captured

        mapParameter.forEach { (key, expectedValue) ->
            val actualRequestParamValue = requestParams.parameters[key]

            assert(actualRequestParamValue == expectedValue) {
                "Request params key \"$key\" value is \"$actualRequestParamValue\". Expected is: $expectedValue"
            }
        }
    }

    private fun `Then assert request params has key ROWS with value 0`() {
        val requestParams = requestParamsSlot.captured
        val rowsValue = requestParams.parameters[SearchApiConst.ROWS]
        assert(rowsValue == "0") {
            "Request Params key \"${SearchApiConst.ROWS}\" is \"$rowsValue\". Expected is: 0"
        }
    }

    @Test
    fun `Get product count fails should call set product count with empty string`() {
        `Given Get Product Count Use Case will fail`()

        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true.toString())
        `When get product count`(mapParameter)

        `Then assert view set product count`("0")
    }

    private fun `Given Get Product Count Use Case will fail`() {
        every { getProductCountUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<String>>().error(TestException())
        }
    }
}