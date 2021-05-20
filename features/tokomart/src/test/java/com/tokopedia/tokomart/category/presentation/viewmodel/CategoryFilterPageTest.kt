package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.tokomart.searchcategory.FilterPageTestHelper
import com.tokopedia.tokomart.searchcategory.FilterPageTestHelper.ApplyFilterTestInterface
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
        )
    }

    @Test
    fun `open filter page first time`() {
        filterPageTestHelper.`test open filter page first time`(mapOf())
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
}