package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.UpdateCartTestHelper
import com.tokopedia.tokomart.searchcategory.jsonToObject
import org.junit.Test

class SearchUpdateCartTest: SearchTestFixtures(), UpdateCartTestHelper.Callback {

    private lateinit var updateCartTestHelper: UpdateCartTestHelper

    override fun setUp() {
        super.setUp()

        updateCartTestHelper = UpdateCartTestHelper(
                searchViewModel,
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
    fun `onViewUpdateCartItems should update quantity in product list`() {
        updateCartTestHelper.`onViewUpdateCartItems should update quantity in product list`()
    }
}