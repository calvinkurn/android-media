package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.AddToCartNonVariantTestHelper
import com.tokopedia.tokomart.searchcategory.AddToCartNonVariantTestHelper.Callback
import com.tokopedia.tokomart.searchcategory.jsonToObject
import org.junit.Test

class CategoryAddToCartNonVariantTest: CategoryTestFixtures(), Callback {

    private val categoryModelJSON = "category/first-page-products-variant-and-non-variant.json"
    private val categoryModel = categoryModelJSON.jsonToObject<CategoryModel>()

    private lateinit var addToCartTestHelper: AddToCartNonVariantTestHelper

    override fun setUp() {
        super.setUp()

        addToCartTestHelper = AddToCartNonVariantTestHelper(
                categoryViewModel,
                addToCartUseCase,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        `Given get category first page use case will be successful`(categoryModel)
    }

    @Test
    fun `test add to cart success`() {
        addToCartTestHelper.`test add to cart success`()
    }

    @Test
    fun `test add to cart failed`() {
        addToCartTestHelper.`test add to cart failed`()
    }
}