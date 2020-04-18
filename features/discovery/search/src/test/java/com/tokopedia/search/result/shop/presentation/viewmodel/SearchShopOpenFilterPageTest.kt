package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.TestException
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopOpenFilterPageTest: SearchShopViewModelTestFixtures() {

    @Test
    fun `Open Filter Page successfully`() {
        `Given view get dynamic filter successfully`()

        `When handle view open filter page`()

        `Then should post event success open filter page`()
    }

    private fun `Given view get dynamic filter successfully`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `When handle view open filter page`() {
        searchShopViewModel.onViewOpenFilterPage()
    }

    private fun `Then should post event success open filter page`() {
        val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

        openFilterPageEvent?.getContentIfNotHandled() shouldBe true
    }

    @Test
    fun `Open Filter Page but Filter Data does not exists`() {
        `Given view get dynamic filter successfully without filter data`()

        `When handle view open filter page`()

        `Then should show error message indicating no filter data exists`()
    }

    private fun `Given view get dynamic filter successfully without filter data`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)

        val dynamicFilterModelWithoutFilterData = DynamicFilterModel()
        dynamicFilterModelWithoutFilterData.data = DataValue()
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModelWithoutFilterData)

        searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
    }

    private fun `Then should show error message indicating no filter data exists`() {
        val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

        openFilterPageEvent?.getContentIfNotHandled() shouldBe false
    }

    @Test
    fun `Open Filter Page after Get Dynamic Filter Successful and then Failed`() {
        val exception = TestException()

        `Given view get dynamic filter successfully`()
        `Given view get dynamic filter failed on second try`(exception)

        `When handle view open filter page`()

        `Then should show error message indicating no filter data exists`()
    }

    private fun `Given view get dynamic filter failed on second try`(exception: Exception) {
        getDynamicFilterUseCase.stubExecute().throws(exception)

        searchShopViewModel.onViewReloadData()
    }
}