package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.draft.domain.usecase.GetAllProductsCountDraftUseCase
import com.tokopedia.product.manage.coroutine.TestCoroutineDispatchers
import com.tokopedia.product.manage.feature.list.domain.ClearAllDraftProductUseCase
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.Before
import org.junit.Rule

abstract class ProductDraftListCountViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getAllProductsCountDraftUseCase: GetAllProductsCountDraftUseCase
    private lateinit var clearAllDraftProductUseCase: ClearAllDraftProductUseCase
    private lateinit var updateUploadingDraftProductUseCase: UpdateUploadingDraftProductUseCase

    protected lateinit var viewModel: ProductDraftListCountViewModel

    @Before
    fun setUp() {
        getAllProductsCountDraftUseCase = mockk(relaxed = true)
        clearAllDraftProductUseCase = mockk(relaxed = true)
        updateUploadingDraftProductUseCase = mockk(relaxed = true)

        viewModel = ProductDraftListCountViewModel(
            getAllProductsCountDraftUseCase,
            clearAllDraftProductUseCase,
            updateUploadingDraftProductUseCase,
            TestCoroutineDispatchers
        )
    }

    protected fun onGetAllDraftCount_thenReturn(draftCount: Long) {
        every { getAllProductsCountDraftUseCase.getData(any()) } returns draftCount
    }

    protected fun onGetAllDraftCount_thenReturn(error: Throwable) {
        every { getAllProductsCountDraftUseCase.getData(any()) } throws error
    }

    protected fun onFetchAllDraftCountWithUpdateUploading_thenReturn(isUploading: Boolean) {
        every { updateUploadingDraftProductUseCase.getData(any()) } returns isUploading
    }

    protected fun onFetchAllDraftCountWithUpdateUploading_thenReturn(error: Throwable) {
        every { updateUploadingDraftProductUseCase.getData(any()) } throws error
    }

    protected fun verifyGetAllDraftCountCalled() {
        verify { getAllProductsCountDraftUseCase.getData(RequestParams.EMPTY) }
    }

    protected fun verifyClearAllDraftCalled() {
        verify { clearAllDraftProductUseCase.getData(RequestParams.EMPTY) }
    }

    protected fun verifyUnsubscribeUseCaseCalled() {
        verifyAll {
            getAllProductsCountDraftUseCase.unsubscribe()
            clearAllDraftProductUseCase.unsubscribe()
            updateUploadingDraftProductUseCase.unsubscribe()
        }
    }
}