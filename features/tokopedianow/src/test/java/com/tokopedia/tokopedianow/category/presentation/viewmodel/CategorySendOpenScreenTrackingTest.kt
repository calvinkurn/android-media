package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.category.domain.model.CategoryTrackerModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CategorySendOpenScreenTrackingTest: CategoryTestFixtures() {

    @Test
    fun `opening category page will send open screen tracking`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        `Then assert open screen tracking event contains url`(categoryModel)
    }

    private fun `Then assert open screen tracking event contains url`(
        categoryModel: CategoryModel,
    ) {
        assertThat(
            tokoNowCategoryViewModel.openScreenTrackingUrlLiveData.value!!,
            `is`(CategoryTrackerModel(
                id = categoryModel.categoryDetail.data.id,
                name = categoryModel.categoryDetail.data.name,
                url = categoryModel.categoryDetail.data.url
            ))
        )
    }
}