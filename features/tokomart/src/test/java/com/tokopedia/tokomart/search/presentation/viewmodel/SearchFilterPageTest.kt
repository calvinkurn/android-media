package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokomart.searchcategory.FilterPageTestHelper
import com.tokopedia.tokomart.searchcategory.FilterPageTestHelper.ApplyFilterTestInterface
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.verify
import org.junit.Test

class SearchFilterPageTest: SearchTestFixtures(), ApplyFilterTestInterface {

    private lateinit var filterPageTestHelper: FilterPageTestHelper

    override fun setUp() {
        super.setUp()

        filterPageTestHelper = FilterPageTestHelper(
                searchViewModel,
                getFilterUseCase,
                getProductCountUseCase,
        )
    }

    @Test
    fun `open filter page first time`() {
        val expectedQueryParamMap = defaultQueryParamMap + mapOf(SearchApiConst.SOURCE to TOKONOW)

        filterPageTestHelper.`test open filter page first time`(expectedQueryParamMap)
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
            getSearchFirstPageUseCase.cancelJobs()
            getSearchFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        }
    }

    @Test
    fun `get filter count success when choosing filter`() {
        val mandatoryParams = createMandatoryParamsForGetFilterCount()

        filterPageTestHelper.`test get filter count success when choosing filter`(mandatoryParams)
    }

    private fun createMandatoryParamsForGetFilterCount(): Map<String, String> {
        return mapOf(
                SearchApiConst.SOURCE to TOKONOW,
                SearchApiConst.DEVICE to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE,
        )
    }

    @Test
    fun `get filter count fail when choosing filter`() {
        val mandatoryParams = createMandatoryParamsForGetFilterCount()

        filterPageTestHelper.`test get filter count fail when choosing filter`(mandatoryParams)
    }
}