package com.tokopedia.tokopedianow.recipelist.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.recipelist.domain.model.RecipeFilterSortDataResponse
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetSortFilterUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class TokoNowRecipeFilterViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getSortFilterUseCase: GetSortFilterUseCase

    protected lateinit var viewModel: TokoNowRecipeFilterViewModel

    @Before
    fun setUp() {
        getSortFilterUseCase = mockk(relaxed = true)

        viewModel = TokoNowRecipeFilterViewModel(
            getSortFilterUseCase,
            CoroutineTestDispatchers
        )
    }

    protected fun onGetSortFilter_thenReturn(response: RecipeFilterSortDataResponse) {
        coEvery { getSortFilterUseCase.execute(any()) } returns response
    }

    protected fun onGetSortFilter_thenReturn(throwable: Throwable) {
        coEvery { getSortFilterUseCase.execute(any()) } throws throwable
    }
}