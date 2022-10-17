package com.tokopedia.tokopedianow.recipeautocomplete.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class TokoNowRecipeAutoCompleteViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TokoNowRecipeAutoCompleteViewModel

    @Before
    fun setUp() {
        viewModel = TokoNowRecipeAutoCompleteViewModel(CoroutineTestDispatchers)
    }
}
