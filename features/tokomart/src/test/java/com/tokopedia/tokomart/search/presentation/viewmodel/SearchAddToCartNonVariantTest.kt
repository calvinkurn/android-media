package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.AddToCartNonVariantTestHelper
import com.tokopedia.tokomart.searchcategory.AddToCartNonVariantTestHelper.Callback
import com.tokopedia.tokomart.searchcategory.jsonToObject
import org.junit.Test

class SearchAddToCartNonVariantTest: SearchTestFixtures(), Callback {

    private val searchModelJSON = "search/first-page-products-variant-and-non-variant.json"
    private val searchModel = searchModelJSON.jsonToObject<SearchModel>()

    private lateinit var addToCartTestHelper: AddToCartNonVariantTestHelper

    override fun setUp() {
        super.setUp()

        addToCartTestHelper = AddToCartNonVariantTestHelper(
                searchViewModel,
                addToCartUseCase,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        `Given get search first page use case will be successful`(searchModel)
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