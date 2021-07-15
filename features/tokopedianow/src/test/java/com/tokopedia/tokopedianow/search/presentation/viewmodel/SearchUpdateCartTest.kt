package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.UpdateCartTestHelper
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import io.mockk.verify
import org.junit.Test

class SearchUpdateCartTest: SearchTestFixtures(), UpdateCartTestHelper.Callback {

    private lateinit var updateCartTestHelper: UpdateCartTestHelper

    override fun setUp() {
        super.setUp()

        updateCartTestHelper = UpdateCartTestHelper(
                tokoNowSearchViewModel,
                getMiniCartListSimplifiedUseCase,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        val searchModel = "search/first-page-products-variant-and-non-variant.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)
    }

    @Test
    fun `onViewResumed should update mini cart and quantity in product list`() {
        updateCartTestHelper.`onViewResumed should update mini cart and quantity in product list`()
    }

    @Test
    fun `onViewResumed should not update mini cart if shop id empty or zero`() {
        `Given choose address data`(ChooseAddressConstant.emptyAddress)
        `Given search view model`()

        `When view resumed`()

        `Then assert get mini cart list is not called`()
    }

    private fun `When view resumed`() {
        tokoNowSearchViewModel.onViewResumed()
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
    fun `update mini cart fail should hide mini cart`() {
        updateCartTestHelper.`update mini cart fail should hide mini cart`()
    }

    @Test
    fun `onViewReloadPage should have product with quantity from mini cart`() {
        updateCartTestHelper.`onViewReloadPage should have product with quantity from mini cart`()
    }
}