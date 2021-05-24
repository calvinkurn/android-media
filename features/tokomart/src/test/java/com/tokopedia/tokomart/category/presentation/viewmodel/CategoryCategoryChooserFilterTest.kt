package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.CategoryChooserFilterTestHelper
import com.tokopedia.tokomart.searchcategory.jsonToObject
import org.junit.Test

class CategoryCategoryChooserFilterTest: CategoryTestFixtures(), CategoryChooserFilterTestHelper.Callback {

    private val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()

    private lateinit var categoryChooserFilterPageTestHelper: CategoryChooserFilterTestHelper

    override fun setUp() {
        super.setUp()

        categoryChooserFilterPageTestHelper = CategoryChooserFilterTestHelper(
                categoryViewModel,
                getProductCountUseCase,
                this
        )
    }

    override fun `Given first page API will be successful`() {
        `Given get category first page use case will be successful`(categoryModel)
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