package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.assertNotProductFeedbackWidget
import com.tokopedia.tokopedianow.searchcategory.assertProductFeedbackWidget
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import org.junit.Assert
import org.junit.Test

class CategoryProductFeedbackTest:CategoryTestFixtures() {
    companion object {
        private const val PRODUCT_FEEDBACK_DATA_VIEW_POSITION = 12
    }

    @Test
    fun `test show product feedback widget`() {
        val categoryModel = "category/first-page-6-products.json".jsonToObject<CategoryModel>()

        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        visitableList[PRODUCT_FEEDBACK_DATA_VIEW_POSITION].assertProductFeedbackWidget()
        Assert.assertEquals(tokoNowCategoryViewModel.isProductFeedbackLoopVisible(),true)
    }

    @Test
    fun `test dont show product feedback widget when total products greater than equal to 8`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
         categoryModel.feedbackFieldToggle.tokonowFeedbackFieldToggle.data.isActive = true
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        visitableList.assertNotProductFeedbackWidget()
        Assert.assertEquals(tokoNowCategoryViewModel.isProductFeedbackLoopVisible(),false)
    }
}
