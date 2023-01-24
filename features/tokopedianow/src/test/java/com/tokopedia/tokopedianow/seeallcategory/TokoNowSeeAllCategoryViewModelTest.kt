package com.tokopedia.tokopedianow.seeallcategory

import com.tokopedia.tokopedianow.common.domain.mapper.CategoryListMapper
import com.tokopedia.tokopedianow.data.createCategoryList
import org.junit.Test

class TokoNowSeeAllCategoryViewModelTest: TokoNowSeeAllCategoryViewModelTestFixture() {

    @Test
    fun `when getting categorylist should run and give the success result`() {
        onGetCategoryList_thenReturn(createCategoryList())

        viewModelTokoNow.getCategoryList("1123")

        verifyGetCategoryListUseCaseCalled()

        val expectedResponse = CategoryListMapper.mapToUiModel(createCategoryList().data)
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting categorylist should throw exception and get the failed result`() {
        onGetCategoryList_thenReturn(Exception())

        viewModelTokoNow.getCategoryList("1123")

        verifyGetCategoryListResponseFail()
    }

}
