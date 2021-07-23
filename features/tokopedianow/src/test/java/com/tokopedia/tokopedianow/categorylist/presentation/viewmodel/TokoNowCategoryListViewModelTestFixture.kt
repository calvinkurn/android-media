package com.tokopedia.tokopedianow.categorylist.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.categorylist.domain.mapper.CategoryListMapper
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokopedianow.util.getOrAwaitValue
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

abstract class TokoNowCategoryListViewModelTestFixture {

    @RelaxedMockK
    lateinit var getCategoryListUseCase: GetCategoryListUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModelTokoNow: TokoNowCategoryListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModelTokoNow = TokoNowCategoryListViewModel(
                getCategoryListUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    protected fun verifyGetCategoryListResponseSuccess(expectedResponse: List<CategoryListItemUiModel>) {
        val actualResponse = viewModelTokoNow.categoryList.getOrAwaitValue()
        Assert.assertEquals(expectedResponse, CategoryListMapper.mapToUiModel((actualResponse as Success).data.data))
    }

    protected fun verifyGetCategoryListResponseFail() {
        val actualResponse = viewModelTokoNow.categoryList.getOrAwaitValue()
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verifyGetHomeLayoutUseCaseCalled() {
        coVerify { getCategoryListUseCase.execute(warehouseId = any(), depth = any()) }
    }

    protected fun onGetCategoryList_thenReturn(categoryListResponse: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute(warehouseId = any(), depth = any()) } returns categoryListResponse
    }

    protected fun onGetCategoryList_thenReturn(errorThrowable: Throwable) {
        coEvery { getCategoryListUseCase.execute(warehouseId = any(), depth = any()) } throws errorThrowable
    }

}