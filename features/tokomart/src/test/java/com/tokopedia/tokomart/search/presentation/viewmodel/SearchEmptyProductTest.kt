package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.EmptyProductTestHelper
import com.tokopedia.tokomart.searchcategory.jsonToObject
import org.junit.Test

class SearchEmptyProductTest: SearchTestFixtures(), EmptyProductTestHelper.Callback {

    private lateinit var emptyProductTestHelper: EmptyProductTestHelper

    override fun setUp() {
        super.setUp()
        emptyProductTestHelper = EmptyProductTestHelper(searchViewModel, this)
    }

    override fun `Given first page product list is empty`() {
        val emptyProductModel = "search/emptyproduct/empty-product.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(emptyProductModel)
    }

    override fun `Given first page product list will be successful`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)
    }

    @Test
    fun `empty product list should show empty product view`() {
        emptyProductTestHelper.`empty product list should show empty product view`()
    }

    @Test
    fun `empty product list because of filter should show filter list`() {
        emptyProductTestHelper.`empty product list because of filter should show filter list`()
    }
}