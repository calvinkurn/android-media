package com.tokopedia.tokopedianow.categorylist.presentation.viewmodel

import com.tokopedia.tokopedianow.categorylist.domain.mapper.CategoryListMapper
import com.tokopedia.tokopedianow.data.createCategoryList
import org.junit.Test

class TokoMartCategoryListViewModelTest: TokoMartCategoryListViewModelTestFixture() {

    @Test
    fun `when getting categorylist should run and give the success result`() {
        onGetCategoryList_thenReturn(createCategoryList())

        viewModel.getCategoryList("1123")

        verifyGetHomeLayoutUseCaseCalled()

        val expectedResponse = CategoryListMapper.mapToUiModel(createCategoryList().data)
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting categorylist should throw exception and get the failed result`() {
        onGetCategoryList_thenReturn(Exception())

        viewModel.getCategoryList("1123")

        verifyGetCategoryListResponseFail()
    }

}