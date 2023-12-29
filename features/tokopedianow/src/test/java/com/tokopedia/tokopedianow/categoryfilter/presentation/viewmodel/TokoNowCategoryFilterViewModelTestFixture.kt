package com.tokopedia.tokopedianow.categoryfilter.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

open class TokoNowCategoryFilterViewModelTestFixture {

    companion object {
        private const val CATEGORY_LEVEL_DEPTH = 2
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TokoNowCategoryFilterViewModel

    private lateinit var getCategoryListUseCase: GetCategoryListUseCase
    private lateinit var addressData: TokoNowLocalAddress

    @Before
    fun setUp() {
        getCategoryListUseCase = mockk()
        addressData = mockk()

        viewModel = TokoNowCategoryFilterViewModel(
            getCategoryListUseCase,
            addressData,
            CoroutineTestDispatchersProvider
        )

        onGetWarehousesData_thenReturn(emptyList())
    }

    protected fun onGetCategoryList_thenReturn(response: CategoryListResponse) {
        coEvery { getCategoryListUseCase.execute(any(), CATEGORY_LEVEL_DEPTH) } returns response
    }

    protected fun onGetCategoryList_thenReturn(error: Throwable) {
        coEvery { getCategoryListUseCase.execute(any(), CATEGORY_LEVEL_DEPTH) } throws error
    }

    protected fun onGetWarehousesData_thenReturn(warehouses: List<WarehouseData>) {
        coEvery { addressData.getWarehousesData() } returns warehouses
    }

    protected fun verifyGetCategoryListCalled() {
        coVerify { getCategoryListUseCase.execute(any(), CATEGORY_LEVEL_DEPTH) }
    }
}
