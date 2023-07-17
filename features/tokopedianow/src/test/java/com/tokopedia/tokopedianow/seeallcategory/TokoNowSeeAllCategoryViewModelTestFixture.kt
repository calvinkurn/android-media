package com.tokopedia.tokopedianow.seeallcategory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokopedianow.common.domain.mapper.CategoryListMapper
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.seeallcategory.persentation.viewmodel.TokoNowSeeAllCategoryViewModel
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

abstract class TokoNowSeeAllCategoryViewModelTestFixture {

    @RelaxedMockK
    lateinit var getCategoryListUseCase: GetCategoryListUseCase

    @RelaxedMockK
    lateinit var addressData: TokoNowLocalAddress

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModelTokoNow: TokoNowSeeAllCategoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModelTokoNow = TokoNowSeeAllCategoryViewModel(
            getCategoryListUseCase,
            addressData,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun verifyGetCategoryListResponseSuccess(expectedResponse: List<CategoryListItemUiModel>) {
        val actualResponse = viewModelTokoNow.categoryList.value
        Assert.assertEquals(expectedResponse, CategoryListMapper.mapToUiModel((actualResponse as Success).data.data))
    }

    protected fun verifyGetCategoryListResponseFail() {
        val actualResponse = viewModelTokoNow.categoryList.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verifyGetCategoryListUseCaseCalled(warehouses: List<WarehouseData>) {
        coVerify { getCategoryListUseCase.execute(warehouses = warehouses, depth = any()) }
    }

    protected fun onGetCategoryList_thenReturn(categoryListResponse: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute(warehouses = any(), depth = any()) } returns categoryListResponse
    }

    protected fun onGetCategoryList_thenReturn(errorThrowable: Throwable) {
        coEvery { getCategoryListUseCase.execute(warehouses = any(), depth = any()) } throws errorThrowable
    }
}
