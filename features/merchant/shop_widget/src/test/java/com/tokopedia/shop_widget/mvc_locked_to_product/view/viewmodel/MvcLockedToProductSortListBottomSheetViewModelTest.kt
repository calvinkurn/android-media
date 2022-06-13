package com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductSortListResponse
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.usecase.MvcLockedToProductGetSortListUseCase
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MvcLockedToProductSortListBottomSheetViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var viewModel: MvcLockedToProductSortListBottomSheetViewModel

    @RelaxedMockK
    lateinit var mvcLockedToProductGetSortListUseCase: MvcLockedToProductGetSortListUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MvcLockedToProductSortListBottomSheetViewModel(
            mvcLockedToProductGetSortListUseCase,
            testCoroutineDispatcherProvider
        )
    }

    @Test
    fun `check whether sortListLiveData value is success`() {
        val mockSelectedSort = getMockSelectedSortUiModel()
        val mockResponse = getMvcLockedToProductSortListResponse()
        coEvery {
            mvcLockedToProductGetSortListUseCase.executeOnBackground()
        } returns mockResponse
        viewModel.getSortListData(mockSelectedSort)
        val sortListLiveDataValue = viewModel.sortListLiveData.value
        assert(sortListLiveDataValue is Success)
        val sortListLiveDataValueTotalSort = (sortListLiveDataValue as Success).data.size
        val mockResponseTotalSort = mockResponse.filterSortProduct.data.sort.size
        assert(sortListLiveDataValueTotalSort == mockResponseTotalSort)
    }

    @Test
    fun `check whether sortListLiveData value is fail if exception thrown`() {
        val mockSelectedSort = getMockSelectedSortUiModel()
        coEvery {
            mvcLockedToProductGetSortListUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.getSortListData(mockSelectedSort)
        val sortListLiveDataValue = viewModel.sortListLiveData.value
        assert(sortListLiveDataValue is Fail)
    }

    private fun getMockSelectedSortUiModel(): MvcLockedToProductSortUiModel {
        return MvcLockedToProductSortUiModel()
    }

    private fun getMvcLockedToProductSortListResponse(): MvcLockedToProductSortListResponse {
        val mockListSort = listOf(
            MvcLockedToProductSortListResponse.FilterSortProduct.Data.Sort(),
            MvcLockedToProductSortListResponse.FilterSortProduct.Data.Sort()
        )
        return MvcLockedToProductSortListResponse(
            MvcLockedToProductSortListResponse.FilterSortProduct(
                MvcLockedToProductSortListResponse.FilterSortProduct.Data(
                    mockListSort
                )
            )
        )
    }

}