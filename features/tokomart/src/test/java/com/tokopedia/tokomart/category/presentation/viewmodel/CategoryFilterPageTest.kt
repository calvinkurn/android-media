package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokomart.searchcategory.FilterPageTestHelper
import com.tokopedia.tokomart.searchcategory.FilterPageTestHelper.ApplyFilterTestInterface
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.verify
import org.junit.Test

class CategoryFilterPageTest: CategoryTestFixtures(), ApplyFilterTestInterface {

    private lateinit var filterPageTestHelper: FilterPageTestHelper

    override fun setUp() {
        super.setUp()

        filterPageTestHelper = FilterPageTestHelper(
                categoryViewModel,
                getFilterUseCase,
                getProductCountUseCase,
        )
    }

    @Test
    fun `open filter page first time`() {
        filterPageTestHelper.`test open filter page first time`(
                mapOf(SearchApiConst.SOURCE to TOKONOW_DIRECTORY)
        )
    }

    @Test
    fun `open filter page cannot be spammed`() {
        filterPageTestHelper.`test open filter page cannot be spammed`()
    }

    @Test
    fun `dismiss filter page`() {
        filterPageTestHelper.`test dismiss filter page`()
    }

    @Test
    fun `open filter page second time should not call API again`() {
        filterPageTestHelper.`test open filter page second time should not call API again`()
    }

    @Test
    fun `apply filter from filter page`() {
        filterPageTestHelper.`test apply filter from filter page`(this)
    }

    override fun `Then assert first page use case is called twice`(
            requestParamsSlot: CapturingSlot<RequestParams>
    ) {
        verify (exactly = 2) {
            getCategoryFirstPageUseCase.cancelJobs()
            getCategoryFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        }
    }

    @Test
    fun `get filter count success when choosing filter`() {
        val mandatoryParams = createMandatoryParamsForGetProductCount()

        filterPageTestHelper.`test get filter count success when choosing filter`(mandatoryParams)
    }

    private fun createMandatoryParamsForGetProductCount(): Map<String, String> {
        return mapOf(
                SearchApiConst.NAVSOURCE to TOKONOW_DIRECTORY,
                SearchApiConst.SOURCE to TOKONOW_DIRECTORY,
                SearchApiConst.DEVICE to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE,
                SearchApiConst.SRP_PAGE_ID to defaultCategoryId.toString(),
        )
    }

    @Test
    fun `get filter count fail when choosing filter`() {
        val mandatoryParams = createMandatoryParamsForGetProductCount()

        filterPageTestHelper.`test get filter count fail when choosing filter`(mandatoryParams)
    }
}