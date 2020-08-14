package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.draft.domain.usecase.GetAllProductsCountDraftUseCase
import com.tokopedia.product.manage.coroutine.TestCoroutineDispatchers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.Before
import org.junit.Rule

abstract class ProductDraftListCountViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getAllProductsCountDraftUseCase: GetAllProductsCountDraftUseCase

    protected lateinit var viewModel: ProductDraftListCountViewModel

    @Before
    fun setUp() {
        getAllProductsCountDraftUseCase = mockk(relaxed = true)

        viewModel = ProductDraftListCountViewModel(
            getAllProductsCountDraftUseCase,
            TestCoroutineDispatchers
        )
    }

    protected fun onGetAllDraftCount_thenReturn(draftCount: Long) {
        every { getAllProductsCountDraftUseCase.getData(any()) } returns draftCount
    }

    protected fun onGetAllDraftCount_thenReturn(error: Throwable) {
        every { getAllProductsCountDraftUseCase.getData(any()) } throws error
    }

    protected fun verifyUnsubscribeUseCaseCalled() {
        verifyAll {
            getAllProductsCountDraftUseCase.unsubscribe()
        }
    }
}