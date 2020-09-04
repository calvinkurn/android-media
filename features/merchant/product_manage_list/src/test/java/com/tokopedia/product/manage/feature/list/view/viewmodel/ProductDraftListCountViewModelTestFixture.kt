package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.draft.domain.usecase.ClearAllDraftProductsUseCase
import com.tokopedia.product.manage.common.feature.draft.domain.usecase.GetAllDraftProductsCountUseCase
import com.tokopedia.product.manage.coroutine.TestCoroutineDispatchers
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

    private lateinit var getAllDraftProductsCountUseCase: GetAllDraftProductsCountUseCase

    private lateinit var clearAllDraftProductsCountUseCase: ClearAllDraftProductsUseCase

    protected lateinit var viewModel: ProductDraftListCountViewModel

    @Before
    fun setUp() {
        getAllDraftProductsCountUseCase = mockk(relaxed = true)
        clearAllDraftProductsCountUseCase = mockk(relaxed = true)

        viewModel = ProductDraftListCountViewModel(
                getAllDraftProductsCountUseCase,
                clearAllDraftProductsCountUseCase,
                TestCoroutineDispatchers
        )
    }

    protected fun onGetAllDraftCount_thenReturn(draftCount: Long) {
        every { getAllDraftProductsCountUseCase.getData(any()) } returns draftCount
    }

    protected fun onGetAllDraftCount_thenReturn(error: Throwable) {
        every { getAllDraftProductsCountUseCase.getData(any()) } throws error
    }

    protected fun verifyGetAllDraftCountCalled() {
        verify { getAllDraftProductsCountUseCase.getData(RequestParams.EMPTY) }
    }

    protected fun verifyClearAllDraftCalled() {
        verify { clearAllDraftProductsCountUseCase.getData(RequestParams.EMPTY) }
    }

    protected fun verifyUnsubscribeUseCaseCalled() {
        verifyAll {
            getAllDraftProductsCountUseCase.unsubscribe()
            clearAllDraftProductsCountUseCase.unsubscribe()
        }
    }
}