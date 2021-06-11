package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.EmptyProductTestHelper
import com.tokopedia.tokomart.searchcategory.jsonToObject
import org.junit.Test

class CategoryEmptyProductTest: CategoryTestFixtures(), EmptyProductTestHelper.Callback {

    private lateinit var emptyProductTestHelper: EmptyProductTestHelper

    override fun setUp() {
        super.setUp()
        emptyProductTestHelper = EmptyProductTestHelper(categoryViewModel, this)
    }

    override fun `Given first page product list is empty`() {
        val emptyProductModel = "category/emptyproduct/empty-product.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(emptyProductModel)
    }

    @Test
    fun `empty product list should show empty product view`() {
        emptyProductTestHelper.`empty product list should show empty product view`()
    }
}