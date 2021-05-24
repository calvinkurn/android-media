package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.CategoryChooserFilterTestHelper
import com.tokopedia.tokomart.searchcategory.jsonToObject
import org.junit.Test

class SearchCategoryChooserFilterTest: SearchTestFixtures(), CategoryChooserFilterTestHelper.Callback {

    private val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

    private lateinit var categoryChooserFilterPageTestHelper: CategoryChooserFilterTestHelper

    override fun setUp() {
        super.setUp()

        categoryChooserFilterPageTestHelper = CategoryChooserFilterTestHelper(
                searchViewModel,
                getProductCountUseCase,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        `Given get search first page use case will be successful`(searchModel)
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
}