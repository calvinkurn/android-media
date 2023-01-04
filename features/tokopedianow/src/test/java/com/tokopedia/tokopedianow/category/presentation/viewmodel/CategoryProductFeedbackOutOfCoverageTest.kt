package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.assertNoProductFeedbackWidget
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import io.mockk.every
import org.junit.Test

class CategoryProductFeedbackOutOfCoverageTest:CategoryTestFixtures() {
        companion object {
            private const val PRODUCT_FEEDBACK_DATA_VIEW_POSITION = 12
        }

        override fun setUp() {
            super.setUp()
            `Given address data null`()
        }

        private fun `Given get warehouse API will be successful`(warehouseResponse: GetStateChosenAddressResponse) {
            every {
                getWarehouseUseCase.getStateChosenAddress(any(), any(), SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
            } answers {
                firstArg<(GetStateChosenAddressResponse) -> Unit>().invoke(warehouseResponse)
            }
        }

        @Test
        fun `test don't show product feedback widget`() {
            val categoryModel = "category/first-page-6-products.json".jsonToObject<CategoryModel>()
            `Given get warehouse API will be successful`(GetStateChosenAddressResponse())
            `Given get category first page use case will be successful`(categoryModel)

            `When view created`()

            val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
            assertNoProductFeedbackWidget(visitableList)
        }

}

