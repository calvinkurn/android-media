package com.tokopedia.tokopedianow.recipehome.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class TokoNowRecipeHomeViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getRecipeListUseCase: GetRecipeListUseCase
    private lateinit var addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase
    private lateinit var removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase
    private lateinit var addressData: TokoNowLocalAddress

    protected lateinit var viewModel: TokoNowRecipeHomeViewModel

    @Before
    fun setUp() {
        getRecipeListUseCase = mockk(relaxed = true)
        addRecipeBookmarkUseCase = mockk(relaxed = true)
        removeRecipeBookmarkUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)

        viewModel = TokoNowRecipeHomeViewModel(
            getRecipeListUseCase,
            addRecipeBookmarkUseCase,
            removeRecipeBookmarkUseCase,
            addressData,
            CoroutineTestDispatchers
        )
    }
}