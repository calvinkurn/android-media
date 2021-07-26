package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper
import com.tokopedia.tokopedianow.searchcategory.AddToCartNonVariantTestHelper.Callback
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import org.junit.Test

class SearchAddToCartNonVariantTest: SearchTestFixtures(), Callback {

    private val searchModelJSON = "search/first-page-products-variant-and-non-variant.json"
    private val searchModel = searchModelJSON.jsonToObject<SearchModel>()

    private lateinit var addToCartTestHelper: AddToCartNonVariantTestHelper

    override fun setUp() {
        super.setUp()

        addToCartTestHelper = AddToCartNonVariantTestHelper(
                tokoNowSearchViewModel,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                getMiniCartListSimplifiedUseCase,
                getRecommendationUseCase,
                userSession,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        `Given get search first page use case will be successful`(searchModel)
    }

    override fun `Given first page API can show recommendation`() {
        val emptyProductSearchModel =
                "search/emptyproduct/empty-product.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(emptyProductSearchModel)
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
    fun `add to cart to increase quantity success`() {
        addToCartTestHelper.`add to cart to increase quantity success`()
    }

    @Test
    fun `add to cart to decrease quantity success`() {
        addToCartTestHelper.`add to cart to decrease quantity success`()
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

    @Test
    fun `delete cart failed`() {
        addToCartTestHelper.`delete cart failed`()
    }

    @Test
    fun `test ATC recom non login should redirect to login page`() {
        addToCartTestHelper.`test ATC recom non login should redirect to login page`()
    }

    @Test
    fun `test add to cart recom item success`() {
        addToCartTestHelper.`test add to cart recom item success`()
    }

    @Test
    fun `test add to cart recom item failed`() {
        addToCartTestHelper.`test add to cart recom item failed`()
    }

    @Test
    fun `add to cart recom item with current quantity should do nothing`() {
        addToCartTestHelper.`add to cart recom item with current quantity should do nothing`()
    }
}