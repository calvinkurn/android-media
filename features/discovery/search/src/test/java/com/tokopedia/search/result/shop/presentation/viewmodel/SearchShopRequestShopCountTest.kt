package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.TestException
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.isNeverExecuted
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.slot
import org.junit.Test

internal class SearchShopRequestShopCountTest : SearchShopDataViewTestFixtures() {

    private val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true)
    @Test
    fun `Handle view get shop count with null parameter`() {
        `When view request shop count`()

        `Then verify get shop count API is not called`()
        `Then verify shop count`(null)
    }

    private fun `When view request shop count`() {
        searchShopViewModel.onViewRequestShopCount(mapParameter)
    }

    private fun `Then verify get shop count API is not called`() {
        getShopCountUseCase.isNeverExecuted()
    }

    private fun `Then verify shop count`(shopCount: String?) {
        searchShopViewModel.getShopCountLiveData().value shouldBe shopCount
    }

    @Test
    fun `Handle view get shop count failed`() {
        `Given get shop count API will fail`()

        `When view request shop count`()

        `Then verify get shop count API is called`()
        `Then verify shop count`("0")
    }

    private fun `Given get shop count API will fail`() {
        getShopCountUseCase.stubExecute().throws(TestException("Test Exception"))
    }

    private fun `Then verify get shop count API is called`() {
        getShopCountUseCase.isExecuted()
    }

    @Test
    fun `Handle view get shop count success`() {
        val shopCount = 12_000
        val requestParamsSlot = slot<RequestParams>()

        `Given get shop count API will success`(requestParamsSlot, shopCount)

        `When view request shop count`()

        `Then verify get shop count API is called`()
        `Then verify request params sent to API`(requestParamsSlot)
        `Then verify shop count`(shopCount.toString())
    }

    private fun `Given get shop count API will success`(requestParamsSlot: CapturingSlot<RequestParams>, shopCount: Int) {
        getShopCountUseCase.stubExecute(requestParamsSlot).returns(shopCount)
    }

    private fun `Then verify request params sent to API`(requestParamsSlot: CapturingSlot<RequestParams>) {
        val requestParams = requestParamsSlot.captured

        mapParameter.forEach { (key, expectedValue) ->
            val actualValue = requestParams.parameters[key]
            actualValue.shouldBe(expectedValue,
                    "Request Params $key is $actualValue. Expected is $expectedValue.")
        }
    }
}