package com.tokopedia.tokomart.categorylist.presentation.viewmodel

import com.tokopedia.tokomart.categorylist.domain.mapper.CategoryListMapper
import com.tokopedia.tokomart.data.createCategoryList
import org.junit.Test

class TokoMartCategoryListViewModelTest: TokoMartCategoryListViewModelTestFixture() {

    @Test
    fun `when getting categorylist should run and give the success result`() {
        onGetCategoryList_thenReturn(createCategoryList().data)

        viewModel.getCategoryList("1123")

        verifyGetHomeLayoutUseCaseCalled()

        val expectedResponse = CategoryListMapper.mapToUiModel(createCategoryList().data)
        verifyGetCategoryListResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting categorylist should throw ticker's exception and get the failed result`() {
        onGetCategoryList_thenReturn(Exception())

        viewModel.getCategoryList("1123")

        verifyGetCategoryListResponseFail()
    }

}