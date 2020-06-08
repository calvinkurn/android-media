package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.search.TestException
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import io.mockk.verify
import org.junit.Test

internal class SearchShopGetDynamicFilterTest: SearchShopViewModelTestFixtures() {

    @Test
    fun `Get Dynamic Filter Successful`() {
        `Given dynamic filter API will be successful`()

        `When handle view is visible and added`()

        `Then assert save dynamic filter is executed`()
        `Then assert dynamic filter response event is success (true)`()
    }

    private fun `Given dynamic filter API will be successful`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    private fun `When handle view is visible and added`() {
        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then assert save dynamic filter is executed`() {
        verify(exactly = 1) {
            searchLocalCacheHandler.saveDynamicFilterModelLocally(
                    SearchShopViewModel.SCREEN_SEARCH_PAGE_SHOP_TAB, dynamicFilterModel)
        }
    }

    private fun `Then assert dynamic filter response event is success (true)`() {
        val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

        getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe true
    }

    @Test
    fun `Get Dynamic Filter Failed`() {
        val exception = TestException()


        `Given dynamic filter API will fail`(exception)

        `When handle view is visible and added`()

        `Then assert exception print stack trace is called`(exception)
        `Then assert save dynamic filter is not executed`()
        `Then assert dynamic filter response event is failed (false)`()
    }

    private fun `Given dynamic filter API will fail`(exception: Exception) {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().throws(exception)
    }

    private fun `Then assert exception print stack trace is called`(exception: TestException) {
        exception.isStackTracePrinted shouldBe true
    }

    private fun `Then assert save dynamic filter is not executed`() {
        verify(exactly = 0) {
            searchLocalCacheHandler.saveDynamicFilterModelLocally(any(), any())
        }
    }

    private fun `Then assert dynamic filter response event is failed (false)`() {
        val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

        getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe false
    }
}