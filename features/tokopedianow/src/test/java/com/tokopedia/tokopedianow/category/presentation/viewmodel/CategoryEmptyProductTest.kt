package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.searchcategory.EmptyProductTestHelper
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_GRID_TITLE
import com.tokopedia.usecase.RequestParams
import io.mockk.verify
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryEmptyProductTest: CategoryTestFixtures(), EmptyProductTestHelper.Callback {

    private lateinit var emptyProductTestHelper: EmptyProductTestHelper

    override fun setUp() {
        super.setUp()
        emptyProductTestHelper = EmptyProductTestHelper(tokoNowCategoryViewModel, this)
    }

    override fun `Given first page product list is empty`(feedbackFieldToggle:Boolean) {
        val emptyProductModel = "category/emptyproduct/empty-product.json".jsonToObject<CategoryModel>()
        if(feedbackFieldToggle)
            emptyProductModel.feedbackFieldToggle.tokonowFeedbackFieldToggle.data.isActive = true
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

    override fun `Then assert empty result visitable list`(visitableList: List<Visitable<*>>) {
        visitableList[2].assertTokonowCategoryGrid()
    }

    private fun Visitable<*>.assertTokonowCategoryGrid() {
        assertThat(this, instanceOf(TokoNowCategoryMenuUiModel::class.java))

        val categoryGridUiModel = this as TokoNowCategoryMenuUiModel

        assertThat(categoryGridUiModel.title, shouldBe(CATEGORY_GRID_TITLE))
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

    @Test
    fun `empty product list with feedback widget active should show feedback view`(){
        emptyProductTestHelper.`empty product list with feedback widget active should show feedback view`()
        Assert.assertEquals(tokoNowCategoryViewModel.isProductFeedbackLoopVisible(),true)
    }
}
