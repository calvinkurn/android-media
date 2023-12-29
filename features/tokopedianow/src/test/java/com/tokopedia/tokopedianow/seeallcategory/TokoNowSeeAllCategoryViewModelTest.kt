package com.tokopedia.tokopedianow.seeallcategory

import com.tokopedia.tokopedianow.common.domain.mapper.CategoryListMapper
import com.tokopedia.tokopedianow.data.createCategoryList
import org.junit.Test

class TokoNowSeeAllCategoryViewModelTest : TokoNowSeeAllCategoryViewModelTestFixture() {

    @Test
    fun `when getting categorylist should run and give the success result`() {
        val warehouses = addressData.getWarehousesData()

        onGetCategoryList_thenReturn(createCategoryList())

        viewModelTokoNow.getCategoryList()

        verifyGetCategoryListUseCaseCalled(warehouses)

        val expectedResponse = CategoryListMapper.mapToUiModel(createCategoryList().data)
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting categorylist should throw exception and get the failed result`() {
        onGetCategoryList_thenReturn(Exception())

        viewModelTokoNow.getCategoryList()

        verifyGetCategoryListResponseFail()
    }
}
