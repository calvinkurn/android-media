package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.UpdateCartTestHelper
import com.tokopedia.tokomart.searchcategory.UpdateCartTestHelper.Callback
import com.tokopedia.tokomart.searchcategory.jsonToObject
import io.mockk.verify
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
    fun `onViewResumed should not update mini cart if shop id empty or zero`() {
        `Given choose address data`(ChooseAddressConstant.emptyAddress)
        `Given category view model`()

        `When view resumed`()

        `Then assert get mini cart list is not called`()
    }

    private fun `When view resumed`() {
        categoryViewModel.onViewResumed()
    }

    private fun `Then assert get mini cart list is not called`() {
        verify(exactly = 0) {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `onViewUpdateCartItems should update quantity in product list`() {
        updateCartTestHelper.`onViewUpdateCartItems should update quantity in product list`()
    }

    @Test
    fun `onViewReloadPage should have product with quantity from mini cart`() {
        updateCartTestHelper.`onViewReloadPage should have product with quantity from mini cart`()
    }
}