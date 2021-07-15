package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.EmptyProductTestHelper
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.usecase.RequestParams
import io.mockk.verify
import org.junit.Test

class CategoryEmptyProductTest: CategoryTestFixtures(), EmptyProductTestHelper.Callback {

    private lateinit var emptyProductTestHelper: EmptyProductTestHelper

    override fun setUp() {
        super.setUp()
        emptyProductTestHelper = EmptyProductTestHelper(tokoNowCategoryViewModel, this)
    }

    override fun `Given first page product list is empty`() {
        val emptyProductModel = "category/emptyproduct/empty-product.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(emptyProductModel)
    }

    override fun `Given first page product list will be successful`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)
    }

    override fun `Then verify first page API is called`(
            count: Int,
            requestParamsSlot: MutableList<RequestParams>,
    ) {
        verify (exactly = count) {
            getCategoryFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        }
    }

    @Test
    fun `empty product list should show empty product view`() {
        emptyProductTestHelper.`empty product list should show empty product view`()
    }

    @Test
    fun `empty product list because of filter should show filter list`() {
        emptyProductTestHelper.`empty product list because of filter should show filter list`()
    }

    @Test
    fun `empty state remove filter`() {
        emptyProductTestHelper.`empty state remove filter`()
    }

    @Test
    fun `empty state remove filter with exclude_ prefix`() {
        emptyProductTestHelper.`empty state remove filter with exclude_ prefix`()
    }
}