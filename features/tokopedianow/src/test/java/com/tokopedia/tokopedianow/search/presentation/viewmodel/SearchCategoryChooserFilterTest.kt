package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.CategoryChooserFilterTestHelper
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.usecase.RequestParams
import io.mockk.verify
import org.junit.Test

class SearchCategoryChooserFilterTest: SearchTestFixtures(), CategoryChooserFilterTestHelper.Callback {

    private val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

    private lateinit var categoryChooserFilterPageTestHelper: CategoryChooserFilterTestHelper

    override fun setUp() {
        super.setUp()

        categoryChooserFilterPageTestHelper = CategoryChooserFilterTestHelper(
                tokoNowSearchViewModel,
                getProductCountUseCase,
                getFilterUseCase,
                this,
        )
    }

    override fun `Given first page use case will be successful`() {
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
    fun `test category chooser cannot be spammed`() {
        categoryChooserFilterPageTestHelper.`test category chooser cannot be spammed`()
    }

    @Test
    fun `test get filter count success from category chooser`() {
        val mandatoryParams = createMandatoryTokonowQueryParams()

        categoryChooserFilterPageTestHelper.`test get filter count success from category chooser`(mandatoryParams)
    }

    @Test
    fun `test get filter count failed from category chooser`() {
        val mandatoryParams = createMandatoryTokonowQueryParams()

        categoryChooserFilterPageTestHelper.`test get filter count failed from category chooser`(mandatoryParams)
    }

    @Test
    fun `test apply filter from category chooser`() {
        categoryChooserFilterPageTestHelper.`test apply filter from category chooser`()
    }

    @Test
    fun `test dismiss category chooser`() {
        categoryChooserFilterPageTestHelper.`test dismiss category chooser`()
    }

    @Test
    fun `test get filter count with exclude filter from category chooser`() {
        `Given search view model`(
            mapOf(
                SearchApiConst.Q to defaultKeyword,
                SearchApiConst.SC to "1332",
            )
        )

        categoryChooserFilterPageTestHelper = CategoryChooserFilterTestHelper(
            tokoNowSearchViewModel,
            getProductCountUseCase,
            getFilterUseCase,
            this
        )

        val mandatoryParams = createMandatoryTokonowQueryParams()

        categoryChooserFilterPageTestHelper.
            `test get filter count with exclude filter from category chooser`(mandatoryParams)
    }
}
