package com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class TokoNowRecipeSearchViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getRecipeListUseCase: GetRecipeListUseCase
    private lateinit var addressData: TokoNowLocalAddress

    protected lateinit var viewModel: TokoNowRecipeSearchViewModel

    @Before
    fun setUp() {
        getRecipeListUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)

        viewModel = TokoNowRecipeSearchViewModel(
            getRecipeListUseCase,
            addressData,
            CoroutineTestDispatchers
        )
    }
}