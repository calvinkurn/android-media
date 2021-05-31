package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.UpdateCartTestHelper
import com.tokopedia.tokomart.searchcategory.UpdateCartTestHelper.Callback
import com.tokopedia.tokomart.searchcategory.jsonToObject
import org.junit.Test

class CategoryUpdateCartTest: CategoryTestFixtures(), Callback {

    private lateinit var updateCartTestHelper: UpdateCartTestHelper

    override fun setUp() {
        super.setUp()

        updateCartTestHelper = UpdateCartTestHelper(
                categoryViewModel,
                getMiniCartListSimplifiedUseCase,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        val categoryModel =
                "category/first-page-products-variant-and-non-variant.json".jsonToObject<CategoryModel>()

        `Given get category first page use case will be successful`(categoryModel)
    }

    @Test
    fun `onViewResumed should update mini cart and quantity in product list`() {
        updateCartTestHelper.`onViewResumed should update mini cart and quantity in product list`()
    }

    @Test
    fun `onViewUpdateCartItems should update quantity in product list`() {
        updateCartTestHelper.`onViewUpdateCartItems should update quantity in product list`()
    }
}