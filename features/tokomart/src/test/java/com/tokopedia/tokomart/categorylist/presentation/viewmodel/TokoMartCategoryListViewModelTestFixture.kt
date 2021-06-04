package com.tokopedia.tokomart.categorylist.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokomart.util.getOrAwaitValue
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

abstract class TokoMartCategoryListViewModelTestFixture {

    @RelaxedMockK
    lateinit var getCategoryListUseCase: GetCategoryListUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TokoMartCategoryListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoMartCategoryListViewModel(
                getCategoryListUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    protected fun verifyGetCategoryListResponseSuccess(expectedResponse: List<CategoryListItemUiModel>) {
        val actualResponse = viewModel.categoryList.getOrAwaitValue()
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyGetCategoryListResponseFail() {
        val actualResponse = viewModel.categoryList.getOrAwaitValue()
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verifyGetHomeLayoutUseCaseCalled() {
        coVerify { getCategoryListUseCase.execute(warehouseId = any(), depth = any()) }
    }

    protected fun onGetCategoryList_thenReturn(layoutResponse: List<CategoryResponse>) {
        coEvery { getCategoryListUseCase.execute(warehouseId = any(), depth = any()) } returns layoutResponse
    }

    protected fun onGetCategoryList_thenReturn(errorThrowable: Throwable) {
        coEvery { getCategoryListUseCase.execute(warehouseId = any(), depth = any()) } throws errorThrowable
    }

}