package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.assertNoProductFeedbackWidget
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import io.mockk.coEvery
import org.junit.Test

class CategoryProductFeedbackOutOfCoverageTest : CategoryTestFixtures() {
    override fun setUp() {
        super.setUp()
        `Given address data null`()
    }

    private fun `Given get warehouse API will be successful`(warehouseResponse: GetStateChosenAddressResponse) {
        coEvery {
            getWarehouseUseCase(SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        } returns warehouseResponse
    }

    @Test
    fun `test don't show product feedback widget`() {
        val categoryModel = "oldcategory/first-page-6-products.json".jsonToObject<CategoryModel>()
        `Given get warehouse API will be successful`(GetStateChosenAddressResponse())
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        assertNoProductFeedbackWidget(visitableList)
    }
}
