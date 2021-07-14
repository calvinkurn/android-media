package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Callback
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import org.junit.Test

class CategoryAddToCartNonVariantTest: CategoryTestFixtures(), Callback {

    private val categoryModelJSON = "category/first-page-products-variant-and-non-variant.json"
    private val categoryModel = categoryModelJSON.jsonToObject<CategoryModel>()

    private lateinit var addToCartTestHelper: AddToCartNonVariantTestHelper

    override fun setUp() {
        super.setUp()

        addToCartTestHelper = AddToCartNonVariantTestHelper(
                tokoNowCategoryViewModel,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                getMiniCartListSimplifiedUseCase,
                userSession,
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

    @Test
    fun `add to cart with current quantity should do nothing`() {
        addToCartTestHelper.`add to cart with current quantity should do nothing`()
    }

    @Test
    fun `add to cart to decrease quantity success`() {
        addToCartTestHelper.`add to cart to decrease quantity success`()
    }

    @Test
    fun `add to cart to increase quantity success`() {
        addToCartTestHelper.`add to cart to increase quantity success`()
    }

    @Test
    fun `add to cart to update quantity failed`() {
        addToCartTestHelper.`add to cart to update quantity failed`()
    }

    @Test
    fun `test ATC non login should redirect to cart page`() {
        addToCartTestHelper.`test ATC non login should redirect to login page`()
    }

    @Test
    fun `delete cart success`() {
        addToCartTestHelper.`test delete cart success`()
    }
}