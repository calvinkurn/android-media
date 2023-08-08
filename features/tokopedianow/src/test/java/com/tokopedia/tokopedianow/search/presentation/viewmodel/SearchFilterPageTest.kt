package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.FilterPageTestHelper
import com.tokopedia.tokopedianow.searchcategory.FilterPageTestHelper.Callback
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.usecase.RequestParams
import io.mockk.verify
import org.junit.Test

class SearchFilterPageTest: SearchTestFixtures(), Callback {

    private val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

    private lateinit var filterPageTestHelper: FilterPageTestHelper

    override fun setUp() {
        super.setUp()

        filterPageTestHelper = FilterPageTestHelper(
                tokoNowSearchViewModel,
                getFilterUseCase,
                getProductCountUseCase,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        `Given get search first page use case will be successful`(searchModel)
    }

    override fun `Then assert first page use case is called twice`(
            requestParamsSlot: MutableList<RequestParams>
    ) {
        verify (exactly = 2) {
            getSearchFirstPageUseCase.cancelJobs()
            getSearchFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        }
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
    fun `open filter page second time should call API again`() {
        filterPageTestHelper.`test open filter page second time should call API again`()
    }

    @Test
    fun `apply filter from filter page`() {
        filterPageTestHelper.`test apply filter from filter page`()
    }

    @Test
    fun `get filter count success when choosing filter`() {
        val mandatoryParams = createMandatoryTokonowQueryParams()

        filterPageTestHelper.`test get filter count success when choosing filter`(mandatoryParams)
    }

    @Test
    fun `get filter count fail when choosing filter`() {
        val mandatoryParams = createMandatoryTokonowQueryParams()

        filterPageTestHelper.`test get filter count fail when choosing filter`(mandatoryParams)
    }

}
