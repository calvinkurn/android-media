package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.TestException
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopGetDynamicFilterTest: SearchShopDataViewTestFixtures() {

    @Test
    fun `Get Dynamic Filter Successful`() {
        `Given dynamic filter API will be successful`()

        `When handle view is visible and added`()

        `Then assert dynamic filter response event is success (true)`()
        `Then assert dynamic filter model`(dynamicFilterModel)
        `Then assert active filter count`(1)
    }

    private fun `Given dynamic filter API will be successful`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `When handle view is visible and added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then assert dynamic filter response event is success (true)`() {
        val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

        getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe true
    }

    private fun `Then assert dynamic filter model`(dynamicFilterModel: DynamicFilterModel?) {
        searchShopViewModel.dynamicFilterModel shouldBe dynamicFilterModel
    }

    private fun `Then assert active filter count`(expectedActiveFilterCount: Int?) {
        searchShopViewModel.getActiveFilterCountLiveData().value shouldBe expectedActiveFilterCount
    }

    @Test
    fun `Get Dynamic Filter Failed`() {
        val exception = TestException()

        `Given dynamic filter API will fail`(exception)

        `When handle view is visible and added`()

        `Then assert exception print stack trace is called`(exception)
        `Then assert dynamic filter response event is failed (false)`()
        `Then assert dynamic filter model`(null)
        `Then assert active filter count`(null)
    }

    private fun `Given dynamic filter API will fail`(exception: Exception) {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().throws(exception)
    }

    private fun `Then assert exception print stack trace is called`(exception: TestException) {
        exception.isStackTracePrinted shouldBe true
    }

    private fun `Then assert dynamic filter response event is failed (false)`() {
        val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

        getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe false
    }
}